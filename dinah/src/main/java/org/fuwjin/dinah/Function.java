package org.fuwjin.dinah;


public interface Function {
   Object invoke(Object... args);

   String name();

   Function restrict(FunctionSignature signature);
}
