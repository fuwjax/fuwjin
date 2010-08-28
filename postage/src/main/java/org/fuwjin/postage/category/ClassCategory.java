package org.fuwjin.postage.category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fuwjin.postage.function.CompositeFunction;
import org.fuwjin.postage.function.ConstructorFunction;
import org.fuwjin.postage.function.FieldAccessFunction;
import org.fuwjin.postage.function.FieldMutatorFunction;
import org.fuwjin.postage.function.InstanceOfFunction;
import org.fuwjin.postage.function.MethodFunction;
import org.fuwjin.postage.function.StaticFieldAccessFunction;
import org.fuwjin.postage.function.StaticFieldMutatorFunction;
import org.fuwjin.postage.function.StaticMethodFunction;

public class ClassCategory extends AbstractCategory {
   private final Class<?> type;

   public ClassCategory(final Class<?> type) {
      super(type.getCanonicalName());
      this.type = type;
   }

   private void addMethods(final CompositeFunction function, final Class<?> type, final Class<?> functionType) {
      if(type != null) {
         for(final Method method: type.getDeclaredMethods()) {
            if(method.getName().equals(function.name())) {
               if(Modifier.isStatic(method.getModifiers())) {
                  function.addFunction(new StaticMethodFunction(method));
               } else {
                  function.addFunction(new MethodFunction(method, functionType));
               }
            }
         }
         if(type.isInterface()) {
            for(final Class<?> iface: type.getInterfaces()) {
               addMethods(function, iface, functionType);
            }
         } else {
            addMethods(function, type.getSuperclass(), functionType);
         }
      }
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
   public void fillFunction(final CompositeFunction function) {
      if("instanceof".equals(function.name())) {
         function.addFunction(new InstanceOfFunction(type));
      } else if("new".equals(function.name())) {
         for(final Constructor<?> c: type.getDeclaredConstructors()) {
            function.addFunction(new ConstructorFunction(c));
         }
      } else {
         addMethods(function, type, type);
         Class<?> cls = type;
         while(cls != null) {
            try {
               final Field field = cls.getDeclaredField(function.name());
               if(Modifier.isStatic(field.getModifiers())) {
                  function.addFunction(new StaticFieldAccessFunction(field));
                  function.addFunction(new StaticFieldMutatorFunction(field));
               } else {
                  function.addFunction(new FieldAccessFunction(field, type));
                  function.addFunction(new FieldMutatorFunction(field, type));
               }
            } catch(final Exception e) {
               // continue
            }
            cls = cls.getSuperclass();
         }
      }
   }
}
