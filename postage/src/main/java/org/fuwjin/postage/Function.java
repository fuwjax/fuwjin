package org.fuwjin.postage;

public interface Function {
   Function curry(Object... args);

   Object invoke(Object... args) throws Failure;

   Object invokeSafe(Object... args);

   String name();

   Function optional(Object arg);

   Signature signature();
}
