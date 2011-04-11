package org.fuwjin.dinah.function;

import org.fuwjin.util.BusinessException;

public class FunctionInvocationException extends BusinessException {
   public FunctionInvocationException(final String pattern, final Object... args) {
      super(pattern, args);
   }

   public FunctionInvocationException(final Throwable cause, final String pattern, final Object... args) {
      super(cause, pattern, args);
   }
}
