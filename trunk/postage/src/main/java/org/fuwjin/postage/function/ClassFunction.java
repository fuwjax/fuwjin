package org.fuwjin.postage.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fuwjin.postage.Category;

public class ClassFunction extends CompositeFunction {
   public ClassFunction(final Category category, final Class<?> type, final String name) {
      super(name, category);
      if("new".equals(name)) {
         for(final Constructor<?> c: type.getDeclaredConstructors()) {
            addFunction(new ConstructorFunction(c));
         }
      } else {
         addMethods(name, type, type);
         Class<?> cls = type;
         while(cls != null) {
            try {
               final Field field = cls.getDeclaredField(name);
               if(Modifier.isStatic(field.getModifiers())) {
                  addFunction(new StaticFieldAccessFunction(field));
                  addFunction(new StaticFieldMutatorFunction(field));
               } else {
                  addFunction(new FieldAccessFunction(field, type));
                  addFunction(new FieldMutatorFunction(field, type));
               }
            } catch(final Exception e) {
               // continue
            }
            cls = cls.getSuperclass();
         }
      }
   }

   private void addMethods(final String name, final Class<?> type, final Class<?> functionType) {
      if(type != null) {
         for(final Method method: type.getDeclaredMethods()) {
            if(method.getName().equals(name)) {
               if(Modifier.isStatic(method.getModifiers())) {
                  addFunction(new StaticMethodFunction(method));
               } else {
                  addFunction(new MethodFunction(method, functionType));
               }
            }
         }
         if(type.isInterface()) {
            for(final Class<?> iface: type.getInterfaces()) {
               addMethods(name, iface, functionType);
            }
         } else {
            addMethods(name, type.getSuperclass(), functionType);
         }
      }
   }
}
