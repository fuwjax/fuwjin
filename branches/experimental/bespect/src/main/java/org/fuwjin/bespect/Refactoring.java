package org.fuwjin.bespect;


public class Refactoring extends MethodAdvice {
   private MethodAdvice[] methods;

   public Refactoring(MethodDef target, MethodAdvice... methods){
      super(target);
      this.methods = methods;
   }

   public MethodAdvice[] helpers(){
      return methods;
   }
}
