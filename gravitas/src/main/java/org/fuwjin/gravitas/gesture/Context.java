package org.fuwjin.gravitas.gesture;

import static org.fuwjin.util.ClassUtils.newProxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.fuwjin.util.BeanHandler;
import org.fuwjin.util.ClassUtils;

import com.google.inject.Inject;
import com.google.inject.Injector;

public abstract class Context {
   @Inject
   private Injector injector;
   private ConcurrentMap<Class<?>, Object> beans = new ConcurrentHashMap<Class<?>, Object>();

   public <T>T bean(final Class<T> type){
      Object bean = beans.get(type);
      if(bean == null){
         bean = newBean(type);
         Object old = beans.putIfAbsent(type, bean);
         if(old != null){
            bean = old;
         }
      }
      return type.cast(bean);
   }
   
   private Object newBean(Class<?> type){
      try{
         return injector.getInstance(type);
      }catch(Exception e){
         Object value = newProxy(type, new BeanHandler());
         injectMembers(type, value);
         return value;
      }
   }

   private void injectMembers(Class<?> type, Object value){
      for(Method method: type.getMethods()){
         if(method.isAnnotationPresent(Inject.class)){
            Class<?>[] params = method.getParameterTypes();
            int len = params.length;
            Object[] args = new Object[len];
            for(int i=0;i<len;i++){
               args[i] = injector.getInstance(params[i]);
            }
            ClassUtils.invoke(value, method, args);
         }
      }
   }

   public Class<?> getType(){
      return getClass();
   }

   public abstract String name();

   public abstract void send(final Object... messages);
}
