package org.fuwjin.pogo;

/**
 * Thrown when a parse fails.
 */
public class PogoException extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new instance.
    * @param message the failure message
    */
   public PogoException(final String message) {
      super(message);
   }

   /**
    * Creates a new instance.
    * @param message the failure message
    * @param cause the failure cause
    */
   public PogoException(final String message, final Throwable cause) {
      super(message, cause);
   }
}
