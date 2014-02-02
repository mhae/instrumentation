package com.mhae.transformer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Entry point 
 * @author michaelhaeuptle
 *
 */
public class Agent {
	public static void premain(String agentArgs, Instrumentation inst) throws FileNotFoundException, IOException {
		inst.addTransformer(new ClassFileTransformerChain(agentArgs));
	}
}
