package org.fuwjin.postage;

public class ConstantFunction extends AbstractFunction {
   private final Object value;

   public ConstantFunction(final String name, final Object value) {
      super(new UnknownSignature(name, 0));
      this.value = value;
   }

   @Override
   public Object invokeSafe(final Object... args) {
      return value;
   }
}
