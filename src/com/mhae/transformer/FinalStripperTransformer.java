package com.mhae.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * Strip the class final modifier from all classes that match a regular expression
 * Useful for making Javaassist generated proxies to work against classes that are final
 * @author michaelhaeuptle
 *
 */
public class FinalStripperTransformer implements ClassFileTransformer {
	
	List<Pattern> patternList= new ArrayList<Pattern>();
	
	public FinalStripperTransformer(Properties properties)
	{
		String name = FinalStripperTransformer.class.getName().substring(FinalStripperTransformer.class.getName().lastIndexOf('.')+1);
		String value = properties.getProperty(name);
		if (value != null) {
			value = value.replace(" ", "");
			String[] patterns = value.split(",");
			for (String pattern : patterns) {
				patternList.add(Pattern.compile(pattern));
			}
		}
	}
	
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
	
		String sourceClassName = className.replace('/', '.');
		
		boolean found = false;
		for (Pattern pattern : patternList) {
			// System.out.println(sourceClassName+": "+pattern);
			if (pattern.matcher(sourceClassName).matches()) {
				found = true;
				break;
			}
		}
		
		if (! found) return classfileBuffer;
		
		System.out.println("! Instrumenting: "+sourceClassName);
		
		byte[] byteCode = classfileBuffer;
		try {
			ClassPool classPoolRef = ClassPool.getDefault();
			CtClass ctClazz = classPoolRef.get(sourceClassName);
			int m = ctClazz.getModifiers() & ~Modifier.FINAL;
			ctClazz.setModifiers(m);
			byteCode = ctClazz.toBytecode();
			ctClazz.detach();
		}
		catch (NotFoundException nfe) {
			// happens when we try to instrument a javaassist modified class (e.g. proxy)
		}
		catch (Throwable ex) {
			ex.printStackTrace();
		}
		return byteCode;
	}
}
