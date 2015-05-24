package org.fuwjin.bespect;

import static java.lang.Thread.currentThread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;

public class BespectAgent {
   
   public static void premain(String agentArgs, Instrumentation inst) throws Exception{
      agentmain(agentArgs, inst);
   }
   
   public static void agentmain(String agentArgs, Instrumentation inst) throws Exception{
      InputStream spec = open(agentArgs);
      BufferedReader reader = new BufferedReader(new InputStreamReader(spec));
      try{
         String line = reader.readLine();
         while(line != null){
            if(line.trim().length() == 0){
               continue;
            }
            if(line.startsWith("explicit ")){
               String name = line.substring("explicit ".length()).trim();
               Class<?> cls = currentThread().getContextClassLoader().loadClass(name);
               inst.retransformClasses(cls);
            }else{
               BespectConfig config = new BespectConfig(line);
               BespectTransformer transformer = new BespectTransformer(config);
               inst.addTransformer(transformer, true);
               inst.setNativeMethodPrefix(transformer, config.getMethodPrefix());
            }
            line = reader.readLine();
         }
      }finally{
         spec.close();
      }
   }
   
   private static InputStream open(String path) throws FileNotFoundException{
      InputStream stream = ClassLoader.getSystemResourceAsStream(path);
      if(stream == null){
         stream = currentThread().getContextClassLoader().getResourceAsStream(path);
      }
      if(stream == null){
         stream = new FileInputStream(path);
      }
      return stream;
   }
}
