package org.fuwjin.bespect;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

public class MethodAdvice implements MethodDef{
   private final MethodDef target;

   public MethodAdvice(MethodDef target){
      this.target = target;
   }

   public int access(){
      return target.access();
   }

   public String name(){
      return target.name();
   }
   
   @Override
   public String className(){
      return target.className();
   }

   @Override
   public String desc(){
      return target.desc();
   }

   @Override
   public String[] exceptions(){
      return target.exceptions();
   }

   @Override
   public String signature(){
      return target.signature();
   }

   public MethodVisitor build(ClassVisitor cv){
      if(target == null){
         return new EmptyVisitor();
      }
      return cv.visitMethod(access(), name(), desc(), signature(), exceptions());
   }
}
