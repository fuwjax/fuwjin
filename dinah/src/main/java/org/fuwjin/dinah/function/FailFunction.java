package org.fuwjin.dinah.function;

import java.lang.reflect.Member;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;

public class FailFunction extends AbstractFunction {
   private final String pattern;
   private final Object[] alertArgs;
   private final Throwable cause;

   public FailFunction(final String name, final String pattern, final Object... args) {
      super(name);
      this.pattern = pattern;
      alertArgs = args;
      cause = null;
   }

   public FailFunction(final String name, final Throwable cause, final String pattern, final Object... args) {
      super(name);
      this.cause = cause;
      this.pattern = pattern;
      alertArgs = args;
   }

   @Override
   protected void invokeWithResult(final InvokeResult result, final Object[] args) {
      if(cause == null) {
         result.alert(pattern, alertArgs);
      } else {
         result.alert(cause, pattern, alertArgs);
      }
   }

   @Override
   protected Member member() {
      return null;
   }

   @Override
   public Function restrict(final FunctionSignature signature) {
      return this;
   }
}
