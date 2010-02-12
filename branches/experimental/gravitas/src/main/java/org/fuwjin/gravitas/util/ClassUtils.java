package org.fuwjin.gravitas.util;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

public final class ClassUtils{
   public static <A extends Annotation>A getAnnotation(Class<?> type, Class<A> annotation){
      while(type != null){
         A ann = type.getAnnotation(annotation);
         if(ann != null){
            return ann;
         }
         type = type.getSuperclass();
      }
      return newProxy(annotation, new AnnotationHandler(annotation));
   }

   public static <T>T newProxy(Class<T> type, InvocationHandler handler){
      return type.cast(newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
   }

   private static Map<Class<?>, Class<?>> wrappers;
   static{
      wrappers = new HashMap<Class<?>, Class<?>>();
      wrappers.put(Boolean.TYPE, Boolean.class);
      wrappers.put(Byte.TYPE, Byte.class);
      wrappers.put(Character.TYPE, Character.class);
      wrappers.put(Double.TYPE, Double.class);
      wrappers.put(Float.TYPE, Float.class);
      wrappers.put(Integer.TYPE, Integer.class);
      wrappers.put(Long.TYPE, Long.class);
      wrappers.put(Short.TYPE, Short.class);
   }

   public static Class<?> getWrapper(Class<?> type){
      Class<?> wrapper = wrappers.get(type);
      if(wrapper == null){
         return type;
      }
      return wrapper;
   }
   
   private ClassUtils(){
      // singleton
   }
   
   static{
      new ClassUtils();
   }
}
