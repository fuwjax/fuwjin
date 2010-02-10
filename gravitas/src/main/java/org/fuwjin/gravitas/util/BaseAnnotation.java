package org.fuwjin.gravitas.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseAnnotation implements Annotation{
   private final Class<? extends Annotation> type;

   public BaseAnnotation(Class<? extends Annotation> type){
      this.type = type;
   }

   @Override
   public Class<? extends Annotation> annotationType(){
      return type;
   }

   @Override
   public String toString(){
      StringBuffer result = new StringBuffer(128);
      result.append('@');
      result.append(type.getName());
      result.append('(');
      boolean firstMember = true;
      for(Method method: getMethods()){
         if(firstMember)
            firstMember = false;
         else
            result.append(", ");
         result.append(method.getName());
         result.append('=');
         result.append(ObjectUtils.toString(getValue(method)));
      }
      result.append(')');
      return result.toString();
   }

   @Override
   public boolean equals(Object o){
      try{
         if(!type.isInstance(o))
            return false;
         for(Method method: getMethods()){
            if(!ObjectUtils.equals(getValue(method), method.invoke(o)))
               return false;
         }
         return true;
      }catch(InvocationTargetException e){
         return false;
      }catch(IllegalAccessException e){
         throw new AssertionError(e);
      }
   }

   @Override
   public int hashCode(){
      int result = 0;
      for(Method method: getMethods()){
         result += (127 * method.getName().hashCode()) ^ ObjectUtils.hashCode(getValue(method));
      }
      return result;
   }

   protected Object getValue(Method method){
      try{
         return method.invoke(this);
      }catch(Exception e){
         throw new AssertionError(e);
      }
   }

   protected Method[] getMethods(){
      return type.getDeclaredMethods();
   }
}
