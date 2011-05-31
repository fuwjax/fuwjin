package org.fuwjin.dinah;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Type;
import java.util.Map;
import org.fuwjin.dinah.function.AbstractFunction;

/**
 * A base implementation for FunctionProvider.
 */
public abstract class AbstractFunctionProvider implements FunctionProvider {
   protected static boolean access(final AccessibleObject obj) {
      if(!obj.isAccessible()) {
         try {
            obj.setAccessible(true);
         } catch(final SecurityException e) {
            return false;
         }
      }
      return true;
   }

   protected static void add(final Map<String, AbstractFunction> functions, final AbstractFunction function) {
      final String name = function.name();
      final AbstractFunction func = functions.get(name);
      if(func == null) {
         functions.put(name, function);
      } else {
         functions.put(name, func.join(function));
      }
   }

   private final Adapter adapter;

   protected AbstractFunctionProvider(final Adapter adapter) {
      this.adapter = adapter;
   }

   public <T>T adapt(final Object value, final Class<T> type) throws AdaptException {
      return (T)adapter.adapt(value, type);
   }

   @Override
   public Object adapt(final Object value, final Type type) throws AdaptException {
      return adapter.adapt(value, type);
   }

   @Override
   public Function getFunction(final FunctionSignature signature) throws NoSuchFunctionException {
      AbstractFunction function = getFunctions(signature.category()).get(signature.name());
      if(function == null) {
         throw new NoSuchFunctionException("No function found for %s", signature);
      }
      function = function.restrict(signature);
      if(AbstractFunction.NULL.equals(function)) {
         throw new NoSuchFunctionException("No function matches arguments for %s", signature);
      }
      return function;
   }

   /**
    * Returns the set of functions for a given category.
    * @param category the function category
    * @return the map of function names to functions in the category
    */
   public abstract Map<String, AbstractFunction> getFunctions(String category);
}
