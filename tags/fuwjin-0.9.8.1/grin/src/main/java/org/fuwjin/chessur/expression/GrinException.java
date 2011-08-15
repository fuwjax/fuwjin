package org.fuwjin.chessur.expression;

import java.util.ArrayList;
import java.util.List;
import org.fuwjin.util.BusinessException;

/**
 * Thrown during a scripted abort.
 */
public class GrinException extends BusinessException {
   private static final long serialVersionUID = 1L;
   private final List<Object> traces = new ArrayList<Object>();

   public GrinException(final Object iSummary, final Object oSummary, final String pattern, final Object... args) {
      super();
      traces.add(concatObject(formatObject(pattern, args), ": ", iSummary, " -> ", oSummary));
      setRichMessage(joinObject("\n", traces));
   }

   /**
    * Creates a new instance.
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public GrinException(final Object iSummary, final Object oSummary, final Throwable cause, final String pattern,
         final Object... args) {
      super(cause);
      traces.add(concatObject(formatObject(pattern, args), ": ", iSummary, " -> ", oSummary));
      setRichMessage(joinObject("\n", traces));
   }

   public GrinException(final String pattern, final Object... args) {
      super();
      traces.add(formatObject(pattern, args));
      setRichMessage(joinObject("\n", traces));
   }

   public GrinException(final Throwable cause, final String pattern, final Object... args) {
      super(cause);
      traces.add(formatObject(pattern, args));
      setRichMessage(joinObject("\n", traces));
   }

   public void append(final Object iSummary, final Object oSummary, final String pattern, final Object... args) {
      traces.add(concatObject(formatObject(pattern, args), ": ", iSummary, " -> ", oSummary));
   }
}
