package org.fuwjin.postage;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCategory implements Category {
   private final String name;
   private final Map<String, Function> functions = new HashMap<String, Function>();

   public AbstractCategory(final String name) {
      this.name = name;
   }

   public final void addFunction(final Function function) {
      functions.put(function.signature().name(), function);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final AbstractCategory o = (AbstractCategory)obj;
         return getClass().equals(obj.getClass()) && name.equals(o.name);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public final Function getFunction(final String name) {
      Function function = functions.get(name);
      if(function == null) {
         function = newFunction(name);
         addFunction(function);
      }
      return function;
   }

   @Override
   public final String name() {
      return name;
   }

   protected abstract Function newFunction(String name);

   @Override
   public String toString() {
      return name;
   }
}
