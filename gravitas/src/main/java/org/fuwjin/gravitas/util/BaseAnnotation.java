package org.fuwjin.gravitas.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class BaseAnnotation implements Annotation{
   private final Class<? extends Annotation> type;

   public BaseAnnotation(final Class<? extends Annotation> type){
      this.type = type;
   }

   @Override
   public Class<? extends Annotation> annotationType(){
      return type;
   }

   @Override
   public boolean equals(final Object o){
      try{
         for(final Method method: getMethods()){
            if(!ObjectUtils.equals(getValue(method), method.invoke(o))){
               return false;
            }
         }
         return type.isInstance(o);
      }catch(final Exception e){
         return false;
      }
   }

   @Override
   public int hashCode(){
      int result = 0;
      for(final Method method: getMethods()){
         result += 127 * method.getName().hashCode() ^ ObjectUtils.hashCode(getValue(method));
      }
      return result;
   }

   @Override
   public String toString(){
      final StringBuffer result = new StringBuffer(128);
      result.append('@');
      result.append(type.getName());
      result.append('(');
      boolean firstMember = true;
      for(final Method method: getMethods()){
         if(firstMember){
            firstMember = false;
         }else{
            result.append(", ");
         }
         result.append(method.getName());
         result.append('=');
         result.append(ObjectUtils.toString(getValue(method)));
      }
      result.append(')');
      return result.toString();
   }

   protected Method[] getMethods(){
      return type.getDeclaredMethods();
   }

   protected abstract Object getValue(final Method method);
}
