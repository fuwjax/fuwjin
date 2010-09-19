package org.fuwjin.postage;

import org.fuwjin.postage.Failure.FailureException;

public interface Function {
   Function curry(Object... args);

   Object invoke(Object... args) throws FailureException;

   Object invokeSafe(Object... args);

   String name();

   Function optional(Object arg);

   Class<?> returnType();

   Signature signature();
}
