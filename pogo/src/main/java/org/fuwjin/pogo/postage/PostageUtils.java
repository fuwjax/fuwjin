package org.fuwjin.pogo.postage;

import org.fuwjin.pogo.postage.ReturnFunction.ReturnValue;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;

/**
 * A bridge class for using legacy Pogo with the new Postage library. This class
 * should be refactored away.
 */
public class PostageUtils {
   /**
    * Invokes the function with the given args. An attempt is made to resolve
    * ReturnValue's correctly.
    * @param function the function to invoke
    * @param args the function arguments
    * @return the function result
    */
   public static Object invoke(final Function function, final Object... args) {
      final Object result = invokeImpl(function, args);
      if(result instanceof ReturnValue) {
         return ((ReturnValue)result).value();
      }
      return result;
   }

   private static Object invokeImpl(final Function function, final Object... args) {
      Function func = function;
      for(final Object arg: args) {
         func = func.optional(arg);
      }
      final Object result = func.invokeSafe();
      if(result instanceof Failure) {
         return result;
      }
      if(func.returnType().equals(void.class)) {
         return args[0];
      }
      if(func.returnType().equals(boolean.class)) {
         if((Boolean)result) {
            return args[0];
         }
         return new Failure(function.name() + " returned false");
      }
      return result;
   }

   /**
    * Invokes function with the given args and returns the first arg. This
    * function is used to manage the return values of the functions for rule
    * references as they can sometimes replace their parent's value.
    * @param function the function to invoke
    * @param args the function arguments
    * @return the function result
    */
   public static Object invokeReturn(final Function function, final Object... args) {
      final Object result = invokeImpl(function, args);
      if(result instanceof Failure) {
         return result;
      }
      if(result instanceof ReturnValue) {
         return ((ReturnValue)result).value();
      }
      return args[0];
   }

   /**
    * Returns true if category is not default.
    * @param category the category to test
    * @return false if the category is default, true otherwise
    */
   public static boolean isCustomCategory(final String category) {
      return !"default".equals(category);
   }

   /**
    * Returns true if the function is not default.
    * @param function the function to test
    * @return false if the category is default, true otherwise
    */
   public static boolean isCustomFunction(final Function function) {
      return !"default".equals(function.name());
   }
}
