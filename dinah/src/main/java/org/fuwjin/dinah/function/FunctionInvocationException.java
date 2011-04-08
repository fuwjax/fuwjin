package org.fuwjin.dinah.function;

public class FunctionInvocationException extends Exception {
   public FunctionInvocationException(final FunctionInvocationException chain, final String pattern,
         final Object... args) {
      super(String.format(pattern, args));
   }

   public FunctionInvocationException(final FunctionInvocationException chain, final Throwable cause,
         final String pattern, final Object... args) {
      super(String.format(pattern, args), cause);
   }
}
