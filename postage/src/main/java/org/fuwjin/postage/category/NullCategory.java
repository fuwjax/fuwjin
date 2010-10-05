package org.fuwjin.postage.category;

import static org.fuwjin.postage.category.InstanceCategory.addFunctions;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionFactory;
import org.fuwjin.postage.function.ConstantFunction;

/**
 * A Support factory for instances.
 */
public class NullCategory implements FunctionFactory {
   private static final Object NULL = new Object() {
      @Override
      public boolean equals(final Object obj) {
         return obj == null;
      }

      @Override
      public int hashCode() {
         return 0;
      }

      @Override
      public String toString() {
         return "null";
      }
   };
   private final FunctionMap map = new FunctionMap();

   /**
    * Creates a new instance.
    */
   public NullCategory() {
      map.put("this", new ConstantFunction(null));
      addFunctions(map, NULL, Object.class);
   }

   @Override
   public boolean equals(final Object obj) {
      return getClass().equals(obj.getClass());
   }

   @Override
   public Function getFunction(final String name) {
      return map.get(name);
   }

   @Override
   public int hashCode() {
      return 0;
   }
}
