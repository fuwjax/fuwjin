package org.fuwjin.bespect;

import static org.objectweb.asm.Type.getMethodDescriptor;

import org.objectweb.asm.Type;

public class PreCallMethodAdvisor implements MethodAdvisor{

   @Override
   public CompositeAdvice advise(Class<?> advice, String prefix, MethodDef target){
      Type[] args = Type.getArgumentTypes(target.desc());
      String methodDesc = getMethodDescriptor(Type.VOID_TYPE, args);
      MethodDef adviceMethod = MethodInfo.direct(advice, target.name(), methodDesc);
      if(adviceMethod == null){
         return null;
      }
      RedirectAdvice redirect = new RedirectAdvice(target, prefix);
      return new CompositeAdvice(redirect, new PreCallAdvice(target, redirect, adviceMethod));
   }
}
