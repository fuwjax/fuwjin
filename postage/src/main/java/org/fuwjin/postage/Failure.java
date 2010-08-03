package org.fuwjin.postage;

public class Failure extends Exception {
   private static final long serialVersionUID = 1L;

   public Failure(final String pattern, final Object... args) {
      super(String.format(pattern, args));
   }

   public Failure(final Throwable cause, final String pattern, final Object... args) {
      super(String.format(pattern, args), cause);
   }
}
