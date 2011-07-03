package org.fuwjin.dinah.provider;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.CompositeFunction;

/**
 * A base implementation for FunctionProvider.
 */
public abstract class AbstractFunctionProvider extends BaseFunctionProvider {
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

   private final Map<String, List<AbstractFunction>> cache;

   protected AbstractFunctionProvider(final Adapter adapter) {
      super(adapter);
      cache = new HashMap<String, List<AbstractFunction>>();
   }

   /**
    * Returns the set of functions for a given category.
    * @param category the function category
    * @return the map of function names to functions in the category
    */
   public void addFunctions(String category) throws AdaptException{
      addFunctions(adapt(category, Type.class));
   }

   protected abstract void addFunctions(Type type);

   @Override
   public AbstractFunction getFunction(final SignatureConstraint constraint) throws NoSuchFunctionException {
      final List<AbstractFunction> functions = new ArrayList<AbstractFunction>();
      for(final AbstractFunction function: getFunctions(constraint)) {
         if(constraint.matches(function.signature())) {
            functions.add(function);
         }
      }
      return CompositeFunction.merge(constraint, functions);
   }

   protected void add(final AbstractFunction function) {
      List<AbstractFunction> functions = cache.get(function.signature().name());
      if(functions == null) {
         functions = new ArrayList<AbstractFunction>();
         cache.put(function.signature().name(), functions);
      }
      functions.add(function);
   }

   private List<AbstractFunction> getFunctions(final SignatureConstraint constraint) throws NoSuchFunctionException {
      try {
         List<AbstractFunction> functions = cache.get(constraint.name());
         if(functions == null) {
            addFunctions(constraint.category());
            functions = cache.get(constraint.name());
            if(functions == null) {
               throw new NoSuchFunctionException("No function found for %s", constraint);
            }
         }
         return functions;
      } catch(final AdaptException e) {
         throw new NoSuchFunctionException(e, "Could not find type for %s", constraint.category());
      }
   }
}
