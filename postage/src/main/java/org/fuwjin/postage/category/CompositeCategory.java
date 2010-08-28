package org.fuwjin.postage.category;

import java.util.List;

import org.fuwjin.postage.Category;
import org.fuwjin.postage.function.CompositeFunction;

public class CompositeCategory extends AbstractCategory {
   private final Category category;
   private final List<Category> fallback;

   public CompositeCategory(final Category category, final List<Category> fallback) {
      super(category.name());
      this.category = category;
      this.fallback = fallback;
   }

   @Override
   public void fillFunction(final CompositeFunction function) {
      function.addFunction(category.getFunction(function.name()));
      for(final Category c: fallback) {
         function.addFunction(c.getFunction(function.name()));
      }
   }
}
