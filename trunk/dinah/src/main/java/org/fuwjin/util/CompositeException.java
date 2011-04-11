package org.fuwjin.util;

import java.util.ArrayList;
import java.util.List;

public class CompositeException extends BusinessException {
   private static final long serialVersionUID = 1L;

   public static Exception compose(final Exception composite, final Exception cause) {
      if(composite == null) {
         return cause;
      }
      if(composite instanceof CompositeException) {
         ((CompositeException)composite).add(cause);
      }
      return new CompositeException(composite, cause);
   }

   private final List<Throwable> causes = new ArrayList<Throwable>();

   private CompositeException(final Exception first, final Exception second) {
      super("No option completed successfully");
      initCause(first);
      add(first);
      add(second);
   }

   public void add(final Exception e) {
      causes.add(e);
   }
}
