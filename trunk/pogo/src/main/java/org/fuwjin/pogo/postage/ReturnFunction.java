package org.fuwjin.pogo.postage;

import org.fuwjin.postage.function.AbstractFunction;

public class ReturnFunction extends AbstractFunction {
   public class ReturnValue {
      private final Object value;

      public ReturnValue(final Object value) {
         this.value = value;
      }

      public Object value() {
         return value;
      }
   }

   public ReturnFunction() {
      super("return", ReturnValue.class, true, Object.class, Object[].class);
   }

   @Override
   public Object tryInvoke(final Object... args) {
      final Object[] var = (Object[])args[1];
      if(var.length > 0) {
         return new ReturnValue(var[var.length - 1]);
      }
      return new ReturnValue(args[0]);
   }
}
