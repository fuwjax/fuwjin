package org.fuwjin.postage;

import java.util.HashMap;
import java.util.Map;

public class Postage {
   public static boolean isSuccess(final Object result) {
      return !(result instanceof Failure);
   }

   private final Map<String, Category> categories = new HashMap<String, Category>();

   public Postage() {
      addCategory(new NullCategory());
      addCategory(new VoidCategory());
      addCategory(new DuckCategory());
   }

   public void addCategory(final Category category) {
      categories.put(category.name(), category);
   }

   public Category getCategory(final String category) {
      Category cat = categories.get(category);
      if(cat == null) {
         cat = newCategory(category);
         addCategory(cat);
      }
      return cat;
   }

   private Class<?> getClass(final String category) {
      try {
         return Class.forName(category);
      } catch(final ClassNotFoundException e) {
         throw new IllegalArgumentException("Category " + category + " does not exist", e);
      }
   }

   public Function getFunction(final String category, final String name) {
      return getCategory(category).getFunction(name);
   }

   protected Category newCategory(final Class<?> type) {
      return new ClassCategory(type);
   }

   protected Category newCategory(final String category) {
      final Class<?> type = getClass(category);
      return newCategory(type);
   }
}
