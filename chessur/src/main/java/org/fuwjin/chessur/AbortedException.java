package org.fuwjin.chessur;

import java.util.ArrayList;
import java.util.List;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.util.BusinessException;
import org.fuwjin.util.StringUtils;

public class AbortedException extends BusinessException {
   private final List<Object> stack = new ArrayList();

   public AbortedException(final String pattern, final Object... args) {
      super(pattern, args);
   }

   public AbortedException(final Throwable cause, final String pattern, final Object... args) {
      super(cause, pattern, args);
   }

   public AbortedException addStackTrace(final Snapshot snapshot, final String pattern, final Object... args) {
      stack.add(new Object() {
         @Override
         public String toString() {
            return format(pattern, args) + ": " + snapshot;
         }
      });
      return this;
   }

   @Override
   public String getMessage() {
      return super.getMessage() + StringUtils.join("\n", stack);
   }
}
