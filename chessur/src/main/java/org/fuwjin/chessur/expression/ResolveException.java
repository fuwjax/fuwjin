package org.fuwjin.chessur.expression;

import java.util.ArrayList;
import java.util.List;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.util.BusinessException;
import org.fuwjin.util.StringUtils;

public class ResolveException extends BusinessException {
   private final List<Object> stack = new ArrayList();

   public ResolveException(final Snapshot snapshot, final String pattern, final Object... args) {
      super(pattern, args);
      addStackTrace(snapshot, "");
   }

   public ResolveException(final Throwable cause, final Snapshot snapshot, final String pattern, final Object... args) {
      super(cause, pattern, args);
      addStackTrace(snapshot, "");
   }

   public ResolveException addStackTrace(final Snapshot snapshot, final String pattern, final Object... args) {
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
