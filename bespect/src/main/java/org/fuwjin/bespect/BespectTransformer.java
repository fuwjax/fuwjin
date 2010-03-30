package org.fuwjin.bespect;

import static org.ow2.util.asm.ClassWriter.COMPUTE_FRAMES;
import static org.ow2.util.asm.ClassWriter.COMPUTE_MAXS;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.ow2.util.asm.ClassReader;
import org.ow2.util.asm.ClassVisitor;
import org.ow2.util.asm.ClassWriter;

public class BespectTransformer implements ClassFileTransformer{
   private final BespectConfig config;

   public BespectTransformer(BespectConfig config){
      this.config = config;
   }

   @Override
   public byte[] transform(ClassLoader loader, final String className, Class<?> classBeingRedefined,
         ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException{
      if(!isAdvisedClass(className)){
         return null;
      }
      try{
         final Class<?> advisor = getLoader(loader).loadClass(config.getAdvisor());
         ClassReader reader = new ClassReader(classfileBuffer);
         ClassWriter writer = new ClassWriter(reader,COMPUTE_FRAMES|COMPUTE_MAXS);
         ClassVisitor adapter = new RedirectAdapter(writer, config.getNativeMethodPrefix(), className, advisor);
         reader.accept(adapter, 0);
         return writer.toByteArray();
      }catch(ClassNotFoundException e){
         System.err.println(config.getAdvisor()+" could not be loaded");
         return null;
      }
   }

   private ClassLoader getLoader(ClassLoader loader){
      return loader != null ? loader : ClassLoader.getSystemClassLoader();
   }

   private boolean isAdvisedClass(String className){
      for(String name: config.getAdvisedClasses()){
         if(name.replace('.', '/').equals(className)){
            return true;
         }
      }
      return false;
   }
}
