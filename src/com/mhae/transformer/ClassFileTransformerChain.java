package com.mhae.transformer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Properties;

/**
 * A class file transformer
 * @author michaelhaeuptle
 *
 */
public class ClassFileTransformerChain implements ClassFileTransformer {
	
	final FinalStripperTransformer finalStripper;
	
	public ClassFileTransformerChain(String agentOptions) throws FileNotFoundException, IOException
	{
		Properties properties = new Properties();
		
		if (agentOptions != null && !agentOptions.isEmpty()) {
			properties.load(new FileInputStream(agentOptions));
		}
		else { // fallback
			InputStream propsStream = ClassFileTransformerChain.class.getResourceAsStream("instrumentation.properties");
			properties.load(propsStream);
		}
		// System.out.println(properties);
		
		finalStripper = new FinalStripperTransformer(properties);
	}
	
	
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
	
		// Potential pattern: chain of instrumenters
		byte[] byteCode = finalStripper.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
				
		return byteCode;
	}
}
