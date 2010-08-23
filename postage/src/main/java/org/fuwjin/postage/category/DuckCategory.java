package org.fuwjin.postage.category;

import java.util.HashSet;
import java.util.Set;

import org.fuwjin.postage.Postage;
import org.fuwjin.postage.function.ClassFunction;
import org.fuwjin.postage.function.CompositeFunction;

public class DuckCategory extends AbstractCategory {
   private static final String ARG_COUNT = "Can't duck type without a target";
   private static final String EXCEPTION = "Can't duck type with a null target";

   public DuckCategory(final Postage postage) {
      super("true", postage);
   }

   @Override
   public boolean equals(final Object obj) {
      return getClass().equals(obj.getClass());
   }

   @Override
   protected CompositeFunction newFunction(final String name) {
      return new CompositeFunction(name, this) {
         private final Set<Class<?>> classes = new HashSet<Class<?>>();

         @Override
         public Object invokeSafe(final Object... args) {
            if(args.length == 0) {
               return failure(ARG_COUNT);
            }
            if(args[0] == null) {
               return failure(EXCEPTION);
            }
            if(classes.add(args[0].getClass())) {
               addFunction(new ClassFunction(DuckCategory.this, args[0].getClass(), name));
            }
            return super.invokeSafe(args);
         }
      };
   }
}
