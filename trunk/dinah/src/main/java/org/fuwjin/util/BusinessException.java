package org.fuwjin.util;

public class BusinessException extends Exception {
   private static final long serialVersionUID = 1L;
   private final Object[] args;

   public BusinessException(final String pattern, final Object... args) {
      super(pattern);
      this.args = args;
   }

   public BusinessException(final Throwable cause, final String pattern, final Object... args) {
      super(pattern, cause);
      this.args = args;
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }

   protected String format(final Object[] formatArgs) {
      return String.format(super.getMessage(), formatArgs);
   }

   @Override
   public String getMessage() {
      if(args != null && args.length > 0) {
         return format(args);
      }
      return super.getMessage();
   }

   @Override
   public StackTraceElement[] getStackTrace() {
      if(getCause() != null && getCause() != this) {
         return getCause().getStackTrace();
      }
      return new StackTraceElement[0];
   }
}
