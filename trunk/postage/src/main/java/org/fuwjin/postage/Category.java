package org.fuwjin.postage;

public interface Category {
   void addFunction(Function function);

   Function getFunction(String name);

   Object invokeFallThrough(CompositeSignature signature, CompositeFailure current, Object... args);

   String name();
}
