package org.fuwjin.postage.category;

import org.fuwjin.postage.function.AbstractFunction;
import org.fuwjin.postage.function.CompositeFunction;
import org.fuwjin.postage.function.ConstantFunction;

public class ConstantCategory extends AbstractCategory {
   private final Object value;

   public ConstantCategory() {
      this("null", null);
   }

   public ConstantCategory(final String name, final Object value) {
      super(name);
      this.value = value;
      addFunction(new AbstractFunction("instanceof", boolean.class, false, Object.class) {
         @Override
         public Object tryInvoke(final Object... args) {
            return value == null ? args[0] == null : false;
         }
      });
   }

   @Override
   public void fillFunction(final CompositeFunction function) {
      if(!"instanceof".equals(function.name())) {
         function.addFunction(new ConstantFunction(function.name(), value));
      }
   }
}
