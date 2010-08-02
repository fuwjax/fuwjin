package org.fuwjin.postage;

public class ClassCategory extends AbstractCategory {
   private final Class<?> type;

   public ClassCategory(final Class<?> type) {
      super(type.getCanonicalName());
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
   protected Function newFunction(final String name) {
      return new ClassFunction(type, name);
   }
}
