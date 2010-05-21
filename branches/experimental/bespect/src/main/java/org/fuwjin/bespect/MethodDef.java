package org.fuwjin.bespect;


public interface MethodDef{
   int access();

   String desc();

   String[] exceptions();

   String className();

   String name();

   String signature();
}