package org.fuwjin.bespect;

import static java.lang.Thread.currentThread;

import java.lang.instrument.Instrumentation;

public class BespectAgent {
   
   public static void premain(String agentArgs, Instrumentation inst) throws Exception{
      agentmain(agentArgs, inst);
   }
   
   public static void agentmain(String agentArgs, Instrumentation inst) throws Exception{
      BespectConfig config = new BespectConfig();
      BespectTransformer transformer = new BespectTransformer(config);
      inst.addTransformer(transformer, !config.getRetransformClasses().isEmpty());
      if(config.getNativeMethodPrefix() != null){
         inst.setNativeMethodPrefix(transformer, config.getNativeMethodPrefix());
      }
      for(String name: config.getRetransformClasses()){
         Class<?> cls = currentThread().getContextClassLoader().loadClass(name);
         inst.retransformClasses(cls);
      }
   }
}
