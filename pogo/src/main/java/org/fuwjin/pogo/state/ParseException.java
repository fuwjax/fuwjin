package org.fuwjin.pogo.state;

import java.util.List;

public class ParseException extends Exception {
   private static String join(final List<?> stack) {
      final StringBuilder builder = new StringBuilder();
      for(final Object elm: stack) {
         builder.append('\n').append(elm);
      }
      return builder.toString();
   }

   public static String message(final PogoPosition position, final String message, final List<?> stack) {
      return "Error parsing @" + position + ": " + message + join(stack);
   }

   public ParseException(final PogoPosition position, final String message, final List<?> stack) {
      super(message(position, message, stack));
   }

   public ParseException(final PogoPosition position, final String message, final Throwable cause, final List<?> stack) {
      super(message(position, message, stack), cause);
   }
}
