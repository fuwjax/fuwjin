/**
 * 
 */
package org.fuwjin.util;

import static java.lang.reflect.Array.newInstance;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class AnnotationHandler extends BaseAnnotation implements InvocationHandler{
   private static final Map<Class<?>, Object> defaults = new HashMap<Class<?>, Object>();
   static{
      defaults.put(Boolean.TYPE, false);
      defaults.put(Byte.TYPE, (byte)0);
      defaults.put(Short.TYPE, (short)0);
      defaults.put(Character.TYPE, '\0');
      defaults.put(Integer.TYPE, 0);
      defaults.put(Long.TYPE, 0L);
      defaults.put(Float.TYPE, 0.0F);
      defaults.put(Double.TYPE, 0.0D);
   }

   public AnnotationHandler(final Class<? extends Annotation> type){
      super(type);
   }

   @Override
   public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable{
      try{
         return method.invoke(this, args);
      }catch(final IllegalArgumentException e){
         return getValue(method);
      }
   }

   @Override
   protected Object getValue(final Method method){
      final Object value = method.getDefaultValue();
      if(value != null){
         return value;
      }
      final Class<?> retType = method.getReturnType();
      if(retType.isArray()){
         return newInstance(retType.getComponentType(), 0);
      }
      return defaults.get(retType);
   }
}