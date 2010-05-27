package org.fuwjin.bespect;

import static org.fuwjin.bespect.MethodInfo.direct;


public class ReplaceCallMethodAdvisor implements MethodAdvisor{

   @Override
   public CompositeAdvice advise(Class<?> advice, String prefix, MethodDef target){
      MethodDef adviceMethod = direct(advice, target.name(), target.desc());
      if(adviceMethod == null){
         return null;
      }
      return new CompositeAdvice(null, new ReplaceCallAdvice(target, adviceMethod));
   }
}
