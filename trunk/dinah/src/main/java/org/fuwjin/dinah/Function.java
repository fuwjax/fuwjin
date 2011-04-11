package org.fuwjin.dinah;


public interface Function {
   Object invoke(Object... args) throws Exception;

   String name();

   Function restrict(FunctionSignature signature);
}
