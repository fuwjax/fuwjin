package org.fuwjin.chessur.expression;

import org.fuwjin.util.BusinessException;

/**
 * Thrown when a script cannot continue.
 */
public class ResolveException extends BusinessException {
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new instance.
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public ResolveException(final String pattern, final Object... args) {
      super(pattern, args);
   }

   /**
    * Creates a new instance.
    * @param cause the cause
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public ResolveException(final Throwable cause, final String pattern, final Object... args) {
      super(cause, pattern, args);
   }

   @Override
   public Throwable getCause() {
      if(super.getCause() instanceof ResolveException) {
         return super.getCause().getCause();
      }
      return super.getCause();
   }

   @Override
   public String getMessage() {
      if(super.getCause() instanceof ResolveException) {
         return super.getCause().getMessage() + "\n" + super.getMessage();
      }
      return super.getMessage();
   }
}
