package org.fuwjin.postage.category;

import java.util.List;

import org.fuwjin.postage.FunctionFactory;
import org.fuwjin.postage.function.CompositeFunction;

public class CompositeCategory extends AbstractCategory {
   private final FunctionFactory category;
   private final List<FunctionFactory> fallback;

   public CompositeCategory(final FunctionFactory category, final List<FunctionFactory> fallback) {
      super(category.name());
      this.category = category;
      this.fallback = fallback;
   }

   @Override
   public void fillFunction(final CompositeFunction function) {
      function.addFunction(category.getFunction(function.name()));
      for(final FunctionFactory c: fallback) {
         function.addFunction(c.getFunction(function.name()));
      }
   }
}
