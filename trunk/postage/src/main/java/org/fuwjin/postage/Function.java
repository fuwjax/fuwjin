package org.fuwjin.postage;

public interface Function {
   Function curry(Object... args);

   Object invoke(Object... args) throws Failure;

   Object invokeSafe(Object... args);

   Signature signature();
}
