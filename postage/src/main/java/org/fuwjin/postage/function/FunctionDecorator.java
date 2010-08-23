package org.fuwjin.postage.function;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Signature;

public class FunctionDecorator implements Function {
   private final Function function;

   public FunctionDecorator(final Function function) {
      this.function = function;
   }

   @Override
   public Function curry(final Object... args) {
      return function.curry(args);
   }

   @Override
   public Object invoke(final Object... args) throws Failure {
      final Object result = invokeSafe(args);
      if(result instanceof Failure) {
         throw (Failure)result;
      }
      return result;
   }

   @Override
   public Object invokeSafe(final Object... args) {
      return function.invokeSafe(args);
   }

   @Override
   public String name() {
      return function.name();
   }

   @Override
   public Function optional(final Object arg) {
      return function.optional(arg);
   }

   @Override
   public Signature signature() {
      return function.signature();
   }
}
