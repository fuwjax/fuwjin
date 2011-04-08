package org.fuwjin.dinah;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.CompositeFunction;
import org.fuwjin.dinah.function.ConstructorFunction;
import org.fuwjin.dinah.function.FieldAccessFunction;
import org.fuwjin.dinah.function.FieldMutatorFunction;
import org.fuwjin.dinah.function.FixedArgsFunction;
import org.fuwjin.dinah.function.InstanceOfFunction;
import org.fuwjin.dinah.function.MethodFunction;
import org.fuwjin.dinah.function.StaticFieldAccessFunction;
import org.fuwjin.dinah.function.StaticFieldMutatorFunction;
import org.fuwjin.dinah.function.StaticMethodFunction;
import org.fuwjin.dinah.function.VarArgsFunction;
import org.fuwjin.util.TypeUtils;

public class ReflectiveTypeFunctionProvider implements FunctionProvider {
   private final Type type;
   private final Map<String, AbstractFunction> functions = new HashMap<String, AbstractFunction>();
   private final String typeName;

   public ReflectiveTypeFunctionProvider(final String typeName) throws Exception {
      this.typeName = typeName;
      type = TypeUtils.forName(typeName);
      add(new InstanceOfFunction(typeName, type));
      for(final Constructor<?> c: TypeUtils.getDeclaredConstructors(type)) {
         if(access(c)) {
            if(c.isVarArgs()) {
               add(new VarArgsFunction(new ConstructorFunction(typeName, c)));
            } else {
               add(new ConstructorFunction(typeName, c));
            }
         }
      }
      addMethods(type, true);
      addFields(type, true);
   }

   private boolean access(final AccessibleObject obj) {
      if(!obj.isAccessible()) {
         try {
            obj.setAccessible(true);
         } catch(final SecurityException e) {
            return false;
         }
      }
      return true;
   }

   private void add(final AbstractFunction function) {
      final String name = function.name();
      final AbstractFunction f = functions.get(name);
      if(f == null) {
         functions.put(name, function);
      } else if(f instanceof CompositeFunction) {
         ((CompositeFunction)f).add(function);
      } else {
         final CompositeFunction composite = new CompositeFunction(f, function);
         functions.put(name, composite);
      }
   }

   private void addFields(final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Field field: TypeUtils.getDeclaredFields(reflectType)) {
            if(access(field)) {
               if(Modifier.isStatic(field.getModifiers())) {
                  if(allowStatic) {
                     add(new StaticFieldAccessFunction(typeName, field));
                     add(new StaticFieldMutatorFunction(typeName, field));
                  }
               } else {
                  add(new FieldAccessFunction(typeName, field, type));
                  add(new FieldMutatorFunction(typeName, field, type));
               }
            }
         }
         addFields(TypeUtils.getSupertype(reflectType), false);
      }
   }

   private void addMethods(final Type reflectType, final boolean allowStatic) {
      if(reflectType != null) {
         for(final Method method: TypeUtils.getDeclaredMethods(reflectType)) {
            if(access(method)) {
               FixedArgsFunction func;
               if(Modifier.isStatic(method.getModifiers())) {
                  if(allowStatic) {
                     func = new StaticMethodFunction(typeName, method);
                  } else {
                     continue;
                  }
               } else {
                  func = new MethodFunction(typeName, method, type);
               }
               if(method.isVarArgs()) {
                  add(new VarArgsFunction(func));
               } else {
                  add(func);
               }
            }
         }
         if(TypeUtils.isInterface(reflectType)) {
            for(final Type iface: TypeUtils.getInterfaces(reflectType)) {
               addMethods(iface, false);
            }
         } else {
            addMethods(TypeUtils.getSupertype(reflectType), false);
         }
      }
   }

   @Override
   public Function getFunction(final FunctionSignature signature) {
      final Function function = functions.get(signature.name());
      return function.restrict(signature);
   }
}
