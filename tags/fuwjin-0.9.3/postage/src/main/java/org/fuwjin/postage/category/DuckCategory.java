package org.fuwjin.postage.category;

import org.fuwjin.postage.function.CompositeFunction;
import org.fuwjin.postage.function.DuckFunction;

public class DuckCategory extends AbstractCategory {
   public DuckCategory() {
      super("true");
   }

   public DuckCategory(final String name) {
      super(name);
   }

   @Override
   public boolean equals(final Object obj) {
      return getClass().equals(obj.getClass());
   }

   @Override
   public void fillFunction(final CompositeFunction function) {
      function.addFunction(new DuckFunction(function.name()));
   }
}
