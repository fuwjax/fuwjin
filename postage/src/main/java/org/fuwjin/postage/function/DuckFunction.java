package org.fuwjin.postage.function;

import java.util.HashSet;
import java.util.Set;

import org.fuwjin.postage.category.ClassCategory;

public class DuckFunction extends CompositeFunction {
   private final Set<Class<?>> classes = new HashSet<Class<?>>();

   public DuckFunction(final String name) {
      super(name);
   }

   @Override
   public Object invokeSafe(final Object... args) {
      if(args.length < 1) {
         return failure("At least one argument required to duck type");
      }
      if(args[0] == null) {
         return failure("Non-null object required to duck type");
      }
      if(classes.add(args[0].getClass())) {
         new ClassCategory(args[0].getClass()).fillFunction(this);
      }
      return super.invokeSafe(args);
   }
}
