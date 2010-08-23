package org.fuwjin.postage.function;

public class ConstantFunction extends AbstractFunction {
   private final Object value;

   public ConstantFunction(final String name, final Object value) {
      super(name, value.getClass(), true, Object[].class);
      this.value = value;
   }

   @Override
   protected Object tryInvoke(final Object... args) {
      return value;
   }
}
