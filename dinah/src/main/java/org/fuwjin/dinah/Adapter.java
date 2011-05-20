package org.fuwjin.dinah;

import java.lang.reflect.Type;
import org.fuwjin.util.BusinessException;

/**
 * Manages transformation of objects into other types.
 */
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

   /**
    * The unset sentinel.
    */
   Object UNSET = new Object() {
      @Override
      public String toString() {
         return "UNSET";
      }
   };

   /**
    * Transforms the value to an instance of the type.
    * @param value the value to transform
    * @param type the type to transform into
    * @return the transformed value
    * @throws AdaptException if the value cannot be transformed into the type
    */
   Object adapt(Object value, Type type) throws AdaptException;
}
