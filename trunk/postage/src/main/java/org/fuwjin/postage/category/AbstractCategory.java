package org.fuwjin.postage.category;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionFactory;
import org.fuwjin.postage.function.CompositeFunction;

public abstract class AbstractCategory implements FunctionFactory {
   private final String name;
   private final Map<String, CompositeFunction> functions = new HashMap<String, CompositeFunction>();

   public AbstractCategory(final String name) {
      this.name = name;
   }

   protected final void addFunction(final Function function) {
      getFunction(function.name()).addFunction(function);
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

   protected abstract void fillFunction(CompositeFunction function);

   @Override
   public final CompositeFunction getFunction(final String name, final Type... parameters) {
      CompositeFunction function = functions.get(name);
      if(function == null) {
         function = new CompositeFunction(name);
         fillFunction(function);
         functions.put(function.name(), function);
      }
      return function;
   }

   @Override
   public final String name() {
      return name;
   }

   @Override
   public String toString() {
      return name;
   }
}
