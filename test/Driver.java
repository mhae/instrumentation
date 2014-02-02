import java.lang.reflect.Method;



import other.Test2;

import javassist.*;
import javassist.util.proxy.*;

/**
 * Simple test driver that verifies whether the final modifier from a class got stripped
 * Call it with -javaagent:./instrumentor.jar=./instrumentation.properties
 * @author michaelhaeuptle
 *
 */
public class Driver
{

	/**
	 * @param args
	 * @throws NotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws NotFoundException, InstantiationException, IllegalAccessException
	{
		System.out.println("Test: "+Modifier.toString(Test.class.getModifiers()));
	
		// Proxies only work with non final classes
		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(Test.class);
		Class<?> clazz = factory.createClass();
		MethodHandler handler = new MethodHandler() {

			@Override
			public Object invoke(Object self, Method overridden, Method forwarder, Object[] args)
					throws Throwable
			{
				System.out.println("do something " + overridden.getName());
				return forwarder.invoke(self, args);
			}

		};
		Object instance = clazz.newInstance();
		((ProxyObject) instance).setHandler(handler);

		System.out.println("Test2: " + Modifier.toString(Test2.class.getModifiers()));
		System.out.println("Test3: " + Modifier.toString(Test3.class.getModifiers()));
	
	}
}
