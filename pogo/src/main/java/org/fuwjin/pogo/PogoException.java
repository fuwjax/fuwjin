package org.fuwjin.pogo;


public class PogoException extends Exception {
   private static final long serialVersionUID = 1L;

   public PogoException(final String message) {
      super(message);
   }

   public PogoException(final String message, final Throwable cause) {
      super(message, cause);
   }
}
