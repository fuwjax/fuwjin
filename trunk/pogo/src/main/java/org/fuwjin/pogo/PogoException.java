package org.fuwjin.pogo;

import java.util.List;

import org.fuwjin.pogo.state.PogoPosition;

public class PogoException extends Exception {
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

   public PogoException(final PogoPosition position, final String message, final List<?> stack) {
      super(message(position, message, stack));
   }

   public PogoException(final PogoPosition position, final String message, final Throwable cause, final List<?> stack) {
      super(message(position, message, stack), cause);
   }
}
