package org.fuwjin.postage.category;

import org.fuwjin.postage.Category;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.UnknownSignature;
import org.fuwjin.postage.function.AbstractFunction;
import org.fuwjin.postage.function.ClassFunction;

public class DuckCategory implements Category {
   private static final String ARG_COUNT = "Can't duck type without a target";
   private static final String EXCEPTION = "Can't duck type with a null target";

   @Override
   public boolean equals(final Object obj) {
      return getClass().equals(obj.getClass());
   }

   protected Function getFunction(final Class<?> type, final String name) {
      return new ClassFunction(type, name);
   }

   @Override
   public Function getFunction(final String name) {
      return new AbstractFunction(new UnknownSignature(name, 1)) {
         @Override
         public Object invokeSafe(final Object... args) {
            if(args.length == 0) {
               return failure(ARG_COUNT);
            }
            if(args[0] == null) {
               return failure(EXCEPTION);
            }
            return getFunction(args[0].getClass(), name).invokeSafe(args);
         }
      };
   }

   @Override
   public String name() {
      return "true";
   }
}
