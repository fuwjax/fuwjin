package org.fuwjin.postage.function;

public class ConstantFunction extends AbstractFunction {
   private final Object value;

   public ConstantFunction(final String name, final Object value) {
      this(name, value, value == null ? Object.class : value.getClass());
   }

   public ConstantFunction(final String name, final Object value, final Class<?> type) {
      super(name, type, true, Object[].class);
      this.value = value;
   }

   @Override
   protected Object tryInvoke(final Object... args) {
      return value;
   }
}
