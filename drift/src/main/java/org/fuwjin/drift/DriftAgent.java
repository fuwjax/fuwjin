package org.fuwjin.drift;

import static org.ow2.util.asm.ClassWriter.COMPUTE_FRAMES;
import static org.ow2.util.asm.ClassWriter.COMPUTE_MAXS;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.ow2.util.asm.ClassReader;
import org.ow2.util.asm.ClassVisitor;
import org.ow2.util.asm.ClassWriter;

public class DriftAgent implements ClassFileTransformer{
   
   public static void premain(String agentArgs, Instrumentation inst) throws Exception{
      agentmain(agentArgs, inst);
   }
   
   public static void agentmain(String agentArgs, Instrumentation inst) throws Exception{
      inst.addTransformer(new DriftAgent(), false);
   }
   
   @Override
   public byte[] transform(ClassLoader loader, final String className, Class<?> classBeingRedefined,
         ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException{
      if(shouldTransform(className)){
         ClassReader reader = new ClassReader(classfileBuffer);
         ClassWriter writer = new ClassWriter(reader,COMPUTE_FRAMES|COMPUTE_MAXS);
         ClassVisitor adapter = new DriftAdapter(className, writer);
         reader.accept(adapter, 0);
         return writer.toByteArray();
      }
      return null;
   }

   private boolean shouldTransform(String className){
      return className.startsWith("org/fuwjin");
   }
}
