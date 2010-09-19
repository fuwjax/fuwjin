package org.fuwjin.pogo;

import java.util.List;

import org.fuwjin.pogo.state.PogoPosition;

/**
 * Thrown when a parse fails for any reason, including an unexpected negative
 * assertion match, a reflection failure, stream I/O exception, or invalid
 * character. An attempt is made to include a "stack trace" of the rules
 * involved in the failure and the start positions of the failure.
 */
public class PogoException extends Exception {
   private static final long serialVersionUID = 1L;

   private static String join(final List<?> stack) {
      final StringBuilder builder = new StringBuilder();
      for(final Object elm: stack) {
         builder.append('\n').append(elm);
      }
      return builder.toString();
   }

   private static String message(final PogoPosition position, final String message, final List<?> stack) {
      return "Error parsing @" + position + ": " + message + join(stack);
   }

   /**
    * Creates a new instance.
    * @param position the position of the failure
    * @param message the failure message
    * @param stack the partially matched rules involved in the failure
    */
   public PogoException(final PogoPosition position, final String message, final List<?> stack) {
      super(message(position, message, stack));
   }

   /**
    * Creates a new instance.
    * @param position the position of the failure
    * @param message the failure message
    * @param cause the exception which caused the failure
    * @param stack the partially matched rules involved in the failure
    */
   public PogoException(final PogoPosition position, final String message, final Throwable cause, final List<?> stack) {
      super(message(position, message, stack), cause);
   }
}
