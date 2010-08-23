package org.fuwjin.postage.category;

import org.fuwjin.postage.Postage;
import org.fuwjin.postage.function.ClassFunction;
import org.fuwjin.postage.function.InstanceOfFunction;

public class ClassCategory extends AbstractCategory {
   private final Class<?> type;

   public ClassCategory(final Class<?> type, final Postage postage) {
      super(type.getCanonicalName(), postage);
      this.type = type;
      addFunction(new InstanceOfFunction(type));
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ClassCategory o = (ClassCategory)obj;
         return type.equals(o.type);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   protected ClassFunction newFunction(final String name) {
      return new ClassFunction(this, type, name);
   }
}
