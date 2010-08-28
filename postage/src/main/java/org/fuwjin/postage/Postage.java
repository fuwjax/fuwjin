package org.fuwjin.postage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuwjin.postage.category.ClassCategory;
import org.fuwjin.postage.category.CompositeCategory;

public class Postage {
   public static boolean isSuccess(final Object result) {
      return !(result instanceof Failure);
   }

   private final Map<String, Category> categories = new HashMap<String, Category>();
   private final List<Category> nullCategories = new ArrayList<Category>();

   public Postage(final Category... extras) {
      for(final Category category: extras) {
         if(category.name() == null) {
            nullCategories.add(category);
         }
      }
      for(final Category category: extras) {
         if(category.name() != null) {
            addCategory(category);
         }
      }
   }

   private CompositeCategory addCategory(final Category category) {
      final CompositeCategory cat = new CompositeCategory(category, nullCategories);
      categories.put(cat.name(), cat);
      return cat;
   }

   public Category getCategory(final String category) {
      Category cat = categories.get(category);
      if(cat == null) {
         cat = newCategory(category);
         cat = addCategory(cat);
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
