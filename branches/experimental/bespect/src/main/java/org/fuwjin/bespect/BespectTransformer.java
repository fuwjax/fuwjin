package org.fuwjin.bespect;

import static org.objectweb.asm.ClassWriter.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class BespectTransformer implements ClassFileTransformer{
   private final BespectConfig config;

   public BespectTransformer(BespectConfig config){
      this.config = config;
   }

   @Override
   public byte[] transform(ClassLoader loader, final String className, Class<?> classBeingRedefined,
         ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException{
      if(config.isAdvised(className)){
         try{
            final Class<?> advisor = getLoader(loader).loadClass(config.getAdvisor());
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader,COMPUTE_MAXS|COMPUTE_FRAMES);
            ClassVisitor adapter = new BespectAdapter(writer, config.getMethodPrefix(), className, advisor);
            reader.accept(adapter, 0);
            return writer.toByteArray();
         }catch(ClassNotFoundException e){
            System.err.println(config.getAdvisor()+" could not be loaded");
         }
      }
      return null;
   }

   private ClassLoader getLoader(ClassLoader loader){
      return loader != null ? loader : ClassLoader.getSystemClassLoader();
   }
}
