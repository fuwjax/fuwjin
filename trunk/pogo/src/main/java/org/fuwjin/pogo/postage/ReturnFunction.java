package org.fuwjin.pogo.postage;

import org.fuwjin.postage.function.AbstractFunction;

/**
 * Returns the last argument.
 */
public class ReturnFunction extends AbstractFunction {
   /**
    * A wrapper for the last argument.
    */
   public class ReturnValue {
      private final Object value;

      private ReturnValue(final Object value) {
         this.value = value;
      }

      /**
       * Returns the wrapped argument.
       * @return the argument
       */
      public Object value() {
         return value;
      }
   }

   /**
    * Creates a new instance.
    */
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
