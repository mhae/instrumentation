ClassTransformer
================

Author: Michael Haeuptle


Simple byte code instrumentation example that removes the final modifier from a class.
This is handy for proxying final classes with JavaAssist which otherwise wouldn't be possible (proxies can't work with final classes because they can't be sub-classed).

Call the ant task "jar" to build the instrumentation agent (see build.xml)

Invoke the program with -javaagent:./instrumentor.jar=./instrumentation.properties to strip the final modifier from all classes that are defined in instrumentation.properties.
See the test Driver in the test folder for an example.

 