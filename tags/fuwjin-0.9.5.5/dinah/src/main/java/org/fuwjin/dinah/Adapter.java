package org.fuwjin.dinah;

import java.lang.reflect.Type;
import org.fuwjin.util.BusinessException;

public interface Adapter {
   /**
    * Exception for Adapter methods.
    */
   public static class AdaptException extends BusinessException {
      private static final long serialVersionUID = 1L;

      /**
       * Creates a new instance.
       * @param pattern the message pattern
       * @param args the message arguments
       */
      public AdaptException(final String pattern, final Object... args) {
         super(pattern, args);
      }

      /**
       * Creates a new instance.
       * @param cause the exception cause
       * @param pattern the message pattern
       * @param args the message arguments
       */
      public AdaptException(final Throwable cause, final String pattern, final Object... args) {
         super(cause, pattern, args);
      }
   }

   Object UNSET = new Object() {
      @Override
      public String toString() {
         return "UNSET";
      }
   };

   Object adapt(Object value, Type type) throws AdaptException;
}
