package org.fuwjin.dinah.function;

import java.lang.reflect.Member;

public class ConstantFunction extends FixedArgsFunction {
   private final Object value;

   public ConstantFunction(final String name, final Object value) {
      super(name);
      this.value = value;
   }

   @Override
   protected Object invokeSafe(final Object[] args) {
      return value;
   }

   @Override
   protected Member member() {
      return null;
   }
}
