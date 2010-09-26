package org.fuwjin.postage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuwjin.postage.category.ClassCategory;
import org.fuwjin.postage.category.CompositeCategory;

/**
 * The interface to shiny happy reflection. Postage exposes Java elements as
 * Functions, standardized execution hooks.
 */
public class Postage implements FunctionFactory {
   /**
    * Returns true if result is not a failure.
    * @param result the possible failure
    * @return false if result is a failure, true otherwise
    */
   public static boolean isSuccess(final Object result) {
      return !(result instanceof Failure);
   }

   private final Map<String, FunctionFactory> categories = new HashMap<String, FunctionFactory>();
   private final List<FunctionFactory> nullCategories = new ArrayList<FunctionFactory>();

   /**
    * Creates a new instance.
    * @param factories custom factories for extending the standard reflection
    *        hooks
    */
   public Postage(final FunctionFactory... factories) {
      for(final FunctionFactory category: factories) {
         if(category.name() == null) {
            nullCategories.add(category);
         }
      }
      for(final FunctionFactory category: factories) {
         if(category.name() != null) {
            addCategory(category);
         }
      }
   }

   private CompositeCategory addCategory(final FunctionFactory category) {
      final CompositeCategory cat = new CompositeCategory(category, nullCategories);
      categories.put(cat.name(), cat);
      return cat;
   }

   private FunctionFactory getCategory(final String category) {
      FunctionFactory cat = categories.get(category);
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

   @Override
   public Function getFunction(final String name, final Type... parameters) {
      final int index = name.lastIndexOf('.');
      if(index >= 0) {
         return getCategory(name.substring(0, index)).getFunction(name.substring(index + 1));
      }
      return getCategory(null).getFunction(name);
   }

   @Override
   public String name() {
      return "";
   }

   protected FunctionFactory newCategory(final Class<?> type) {
      return new ClassCategory(type);
   }

   protected FunctionFactory newCategory(final String category) {
      final Class<?> type = getClass(category);
      return newCategory(type);
   }
}
