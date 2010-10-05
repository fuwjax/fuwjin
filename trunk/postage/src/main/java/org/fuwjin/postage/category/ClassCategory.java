package org.fuwjin.postage.category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionFactory;
import org.fuwjin.postage.function.ConstructorFunction;
import org.fuwjin.postage.function.FieldAccessFunction;
import org.fuwjin.postage.function.FieldMutatorFunction;
import org.fuwjin.postage.function.InstanceOfFunction;
import org.fuwjin.postage.function.MethodFunction;
import org.fuwjin.postage.function.StaticFieldAccessFunction;
import org.fuwjin.postage.function.StaticFieldMutatorFunction;
import org.fuwjin.postage.function.StaticMethodFunction;

/**
 * A factory for a single class. This class buffers the entire contents of the
 * class on construction and includes an instanceof method. The signature of the
 * virtual instanceof method is
 * 
 * <pre>
 * boolean instanceof(Object.class);
 * </pre>
 */
public class ClassCategory implements FunctionFactory {
   private final Class<?> type;
   private final FunctionMap map = new FunctionMap();

   /**
    * Creates a new instance.
    * @param type the type used to generate the functions
    */
   public ClassCategory(final Class<?> type) {
      this(type, true);
   }

   /**
    * Creates a new instance.
    * @param type the type used to generate the functions
    * @param allowStatic true if statics, constructors and the virtual
    *        instanceof should be included
    */
   public ClassCategory(final Class<?> type, final boolean allowStatic) {
      this.type = type;
      if(allowStatic) {
         map.put("instanceof", new InstanceOfFunction(type));
         for(final Constructor<?> c: type.getDeclaredConstructors()) {
            map.put("new", ConstructorFunction.constructor(c));
         }
      }
      addMethods(type, allowStatic);
      addFields(type, allowStatic);
   }

   private void addFields(final Class<?> reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Field field: reflectType.getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers())) {
               if(allowStatic) {
                  map.put(field.getName(), new StaticFieldAccessFunction(field));
                  map.put(field.getName(), new StaticFieldMutatorFunction(field));
               }
            } else {
               map.put(field.getName(), new FieldAccessFunction(field, type));
               map.put(field.getName(), new FieldMutatorFunction(field, type));
            }
         }
         addFields(reflectType.getSuperclass(), false);
      }
   }

   private void addMethods(final Class<?> reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Method method: reflectType.getDeclaredMethods()) {
            if(Modifier.isStatic(method.getModifiers())) {
               if(allowStatic) {
                  map.put(method.getName(), StaticMethodFunction.method(method));
               }
            } else {
               map.put(method.getName(), MethodFunction.method(method, type));
            }
         }
         if(reflectType.isInterface()) {
            for(final Class<?> iface: reflectType.getInterfaces()) {
               addMethods(iface, false);
            }
         } else {
            addMethods(reflectType.getSuperclass(), false);
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
   public Function getFunction(final String name) {
      return map.get(name);
   }

   @Override
   public int hashCode() {
      return type.hashCode();
   }
}
