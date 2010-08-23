package org.fuwjin.postage;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.postage.category.ClassCategory;
import org.fuwjin.postage.category.DuckCategory;
import org.fuwjin.postage.category.GlobalCategory;
import org.fuwjin.postage.category.NullCategory;
import org.fuwjin.postage.category.VoidCategory;

public class Postage {
   public static boolean isSuccess(final Object result) {
      return !(result instanceof Failure);
   }

   private final Map<String, Category> categories = new HashMap<String, Category>();
   private final Category global;

   public Postage(final Function... globals) {
      global = new GlobalCategory(this);
      addCategory(new NullCategory(this));
      addCategory(new VoidCategory(this));
      addCategory(new DuckCategory(this));
      for(final Function function: globals) {
         global.addFunction(function);
      }
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

   public Object invokeGlobal(final CompositeSignature signature, final CompositeFailure current, final Object... args) {
      final Object result = global.getFunction(signature.name()).invokeSafe(args);
      if(result instanceof Failure) {
         current.addFailure((Failure)result);
         return current;
      }
      return result;
   }

   protected Category newCategory(final Class<?> type) {
      return new ClassCategory(type, this);
   }

   protected Category newCategory(final String category) {
      final Class<?> type = getClass(category);
      return newCategory(type);
   }
}
