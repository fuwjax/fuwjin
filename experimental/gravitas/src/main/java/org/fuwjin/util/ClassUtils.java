package org.fuwjin.util;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class ClassUtils{
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
   static{
      new ClassUtils();
   }

   public static Class<?> getWrapper(final Class<?> type){
      final Class<?> wrapper = wrappers.get(type);
      if(wrapper == null){
         return type;
      }
      return wrapper;
   }

   public static <T>T newProxy(final Class<T> type, final InvocationHandler handler){
      return type.cast(newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
   }
   
   public static <T> T newBean(final Class<T> type){
      return newProxy(type, new BeanHandler());
   }

   private ClassUtils(){
      // singleton
   }

   public static void invoke(Object target, Method method, Object[] args){
      try{
         method.invoke(target, args);
      }catch(InvocationTargetException e){
         throw new RuntimeException(e.getCause());
      }catch(Exception e){
         throw new RuntimeException(e);
      }
   }
}
