package org.fuwjin.postage.category;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.postage.Category;
import org.fuwjin.postage.CompositeFailure;
import org.fuwjin.postage.CompositeSignature;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.function.CompositeFunction;

public abstract class AbstractCategory implements Category {
   private final String name;
   private final Map<String, CompositeFunction> functions = new HashMap<String, CompositeFunction>();
   private final Postage postage;

   public AbstractCategory(final String name, final Postage postage) {
      this.name = name;
      this.postage = postage;
   }

   @Override
   public final void addFunction(final Function function) {
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

   @Override
   public final CompositeFunction getFunction(final String name) {
      CompositeFunction function = functions.get(name);
      if(function == null) {
         function = newFunction(name);
         functions.put(function.name(), function);
      }
      return function;
   }

   @Override
   public Object invokeFallThrough(final CompositeSignature signature, final CompositeFailure current,
         final Object... args) {
      return postage.invokeGlobal(signature, current, args);
   }

   @Override
   public final String name() {
      return name;
   }

   protected abstract CompositeFunction newFunction(String name);

   @Override
   public String toString() {
      return name;
   }
}
