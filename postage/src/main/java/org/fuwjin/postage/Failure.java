package org.fuwjin.postage;

import java.lang.reflect.Type;
import java.util.Arrays;

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

   public static boolean isRecoverable(final Object result) {
      return result instanceof Failure && !((Failure)result).unrecoverable;
   }

   public static boolean isSuccess(final Object object) {
      return !(object instanceof Failure);
   }

   public static Object types(final Type[] parameters) {
      return new Object() {
         @Override
         public String toString() {
            return Arrays.toString(parameters);
         }
      };
   }

   public static Object typesOf(final Object[] arguments) {
      return new Object() {
         @Override
         public String toString() {
            final Type[] types = new Type[arguments.length];
            for(int i = 0; i < arguments.length; i++) {
               types[i] = arguments[i] == null ? null : arguments[i].getClass();
            }
            return types(types).toString();
         }
      };
   }

   private final String pattern;
   private final Object[] args;
   private final Throwable cause;
   private final boolean unrecoverable;

   public Failure(final boolean unrecoverable, final String pattern, final Object... args) {
      this(unrecoverable, null, pattern, args);
   }

   public Failure(final boolean unrecoverable, final Throwable cause, final String pattern, final Object... args) {
      this.unrecoverable = unrecoverable;
      this.cause = cause;
      this.pattern = pattern;
      this.args = args;
   }

   public Failure(final String pattern, final Object... args) {
      this(false, null, pattern, args);
   }

   public Failure(final Throwable cause, final String pattern, final Object... args) {
      this(false, cause, pattern, args);
   }

   public FailureException exception() {
      if(cause == null) {
         return new FailureException(String.format(pattern, args));
      }
      return new FailureException(String.format(pattern, args), cause);
   }
}
