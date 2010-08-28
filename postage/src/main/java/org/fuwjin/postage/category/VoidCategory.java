package org.fuwjin.postage.category;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.function.CompositeFunction;

public class VoidCategory extends AbstractCategory {
   public VoidCategory() {
      super("void");
   }

   public VoidCategory(final String name, final Function... functions) {
      super(name);
      for(final Function function: functions) {
         addFunction(function);
      }
   }

   @Override
   public void fillFunction(final CompositeFunction function) {
   }
}
