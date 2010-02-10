package org.fuwjin.gravitas.util;

import static java.lang.reflect.Array.newInstance;
import static org.fuwjin.gravitas.util.ProxyUtils.newProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationUtils{

   public static <A extends Annotation> A getAnnotation(Class<?> type, Class<A> annotation){
      while(type != null){
         A ann = type.getAnnotation(annotation);
         if(ann != null){
            return ann;
         }
         type = type.getSuperclass();
      }
      return newProxy(annotation, new AnnotationHandler(annotation));
   }
   
   private static final Map<Class<?>,Object> defaults = new HashMap<Class<?>,Object>();
   static{
      defaults.put(Boolean.TYPE,false);
      defaults.put(Byte.TYPE,(byte)0);
      defaults.put(Short.TYPE,(short)0);
      defaults.put(Character.TYPE,(char)0);
      defaults.put(Integer.TYPE,0);
      defaults.put(Long.TYPE,0L);
      defaults.put(Float.TYPE,0.0F);
      defaults.put(Double.TYPE,0.0D);
   }
   
   private static class AnnotationHandler extends BaseAnnotation implements InvocationHandler{
      public AnnotationHandler(Class<? extends Annotation> type){
         super(type);
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
         try{
            return method.invoke(this,args);
         }catch(IllegalArgumentException e){
            return getValue(method);
         }
      }
      
      @Override
      protected Object getValue(Method method){
         Object value = method.getDefaultValue();
         if(value != null){
            return value;
         }
         Class<?> retType = method.getReturnType();
         if(retType.isArray()){
            return newInstance(retType.getComponentType(), 0);
         }
         return defaults.get(retType);
      }
   }
}
