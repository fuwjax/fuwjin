package org.fuwjin.dinah;


public class NoSuchFunctionException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public NoSuchFunctionException(final String pattern, final Object... args) {
      super(pattern);
   }

   public NoSuchFunctionException(final Throwable cause, final String pattern, final Object... args) {
      super(pattern, cause);
   }
}
