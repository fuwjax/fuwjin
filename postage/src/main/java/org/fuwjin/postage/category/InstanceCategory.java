package org.fuwjin.postage.category;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fuwjin.postage.function.CompositeFunction;
import org.fuwjin.postage.function.InstanceFieldAccessFunction;
import org.fuwjin.postage.function.InstanceFieldMutatorFunction;
import org.fuwjin.postage.function.InstanceMethodFunction;

public class InstanceCategory extends AbstractCategory {
   private final Object object;

   public InstanceCategory(final String name, final Object object) {
      super(name);
      this.object = object;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceCategory o = (InstanceCategory)obj;
         return object.equals(o.object);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public void fillFunction(final CompositeFunction function) {
      Class<?> cls = object.getClass();
      while(cls != null) {
         for(final Method method: cls.getDeclaredMethods()) {
            if(method.getName().equals(function.name())) {
               if(!Modifier.isStatic(method.getModifiers())) {
                  function.addFunction(new InstanceMethodFunction(method, object));
               }
            }
         }
         try {
            final Field field = cls.getDeclaredField(function.name());
            if(!Modifier.isStatic(field.getModifiers())) {
               function.addFunction(new InstanceFieldAccessFunction(field, object));
               function.addFunction(new InstanceFieldMutatorFunction(field, object));
            }
         } catch(final Exception e) {
            // continue
         }
         cls = cls.getSuperclass();
      }
   }
}
