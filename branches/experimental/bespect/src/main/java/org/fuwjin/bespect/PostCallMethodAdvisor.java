package org.fuwjin.bespect;

import static org.objectweb.asm.Type.getMethodDescriptor;
import static org.objectweb.asm.Type.getReturnType;

import org.objectweb.asm.Type;

public class PostCallMethodAdvisor implements MethodAdvisor{

   @Override
   public Refactoring advise(Class<?> advice, String prefix, MethodDef target){
      Type returnType = getReturnType(target.desc());
      String methodDesc = getMethodDescriptor(returnType, new Type[]{returnType});
      MethodDef adviceMethod = MethodInfo.direct(advice, target.name(), methodDesc);
      if(adviceMethod == null){
         return null;
      }
      RedirectAdvice redirect = new RedirectAdvice(target, prefix);
      return new Refactoring(redirect, new PostCallAdvice(target, redirect, adviceMethod));
   }
}
