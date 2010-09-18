package org.fuwjin.pogo.postage;

import org.fuwjin.pogo.postage.ReturnFunction.ReturnValue;
import org.fuwjin.postage.Category;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;

public class PostageUtils {
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

   public static boolean isCustomCategory(final Category category) {
      return !"default".equals(category.name());
   }

   public static boolean isCustomFunction(final Function function) {
      return !"default".equals(function.name());
   }
}
