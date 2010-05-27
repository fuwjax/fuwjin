package org.fuwjin.bespect;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;


public class CompositeAdvice extends MethodAdvice {
   private MethodAdvice[] methods;

   public CompositeAdvice(MethodDef target, MethodAdvice... methods){
      super(target);
      this.methods = methods;
   }

   @Override
   public MethodVisitor build(ClassVisitor cv){
      for(MethodAdvice m: methods){
         m.build(cv);
      }
      return super.build(cv);
   }
}
