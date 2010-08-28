package org.fuwjin.postage;

public class Failure {
   public static class FailureException extends Exception {
      public FailureException(final String message) {
         super(message);
      }

      public FailureException(final String message, final Throwable cause) {
         super(message, cause);
      }
   }

   private static final long serialVersionUID = 1L;

   public static Object assertResult(final Object object) throws FailureException {
      if(object instanceof Failure) {
         throw ((Failure)object).exception();
      }
      return object;
   }

   private final String pattern;
   private final Object[] args;
   private final Throwable cause;

   public Failure(final String pattern, final Object... args) {
      this.pattern = pattern;
      this.args = args;
      cause = null;
   }

   public Failure(final Throwable cause, final String pattern, final Object... args) {
      this.cause = cause;
      this.pattern = pattern;
      this.args = args;
   }

   public FailureException exception() {
      if(cause == null) {
         return new FailureException(String.format(pattern, args));
      }
      return new FailureException(String.format(pattern, args), cause);
   }
}
