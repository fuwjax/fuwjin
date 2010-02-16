package org.fuwjin.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeUtils{
   private TypeUtils(){}
   static{
      new TypeUtils();
   }
   
   public static Class<?> getParameterType(final Type type, final int index){
      final ParameterizedType ptype = (ParameterizedType)type;
      return (Class<?>)ptype.getActualTypeArguments()[index];
   }


}
