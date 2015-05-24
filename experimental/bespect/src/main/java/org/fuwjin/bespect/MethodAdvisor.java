package org.fuwjin.bespect;


public interface MethodAdvisor{
   MethodAdvice advise(Class<?> advice, String prefix, MethodDef target);
}
