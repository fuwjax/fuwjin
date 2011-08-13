package org.fuwjin.chessur.expression;

/**
 * Thrown during a scripted abort.
 */
public class AbortedException extends GrinException {
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new instance.
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public AbortedException(final Object iSummary, final Object oSummary, final String pattern, final Object... args) {
      super(iSummary, oSummary, pattern, args);
   }

   /**
    * Creates a new instance.
    * @param cause the cause
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public AbortedException(final Object iSummary, final Object oSummary, final Throwable cause, final String pattern,
         final Object... args) {
      super(iSummary, oSummary, cause, pattern, args);
   }
}
