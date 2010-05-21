package org.fuwjin.bespect;


public interface MethodAdvisor{
   Refactoring advise(Class<?> advice, String prefix, MethodDef target);
}
