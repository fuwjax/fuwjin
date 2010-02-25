package org.fuwjin.util;

import java.util.Arrays;

public final class ObjectUtils{
   static{
      new ObjectUtils();
   }

   public static boolean equals(final Object value, final Object oValue){
      if(value == null){
         return oValue == null;
      }
      final Class<?> type = value.getClass();
      if(!type.isArray()){
         return value.equals(oValue);
      }
      if(type == byte[].class){
         return Arrays.equals((byte[])value, (byte[])oValue);
      }
      if(type == char[].class){
         return Arrays.equals((char[])value, (char[])oValue);
      }
      if(type == double[].class){
         return Arrays.equals((double[])value, (double[])oValue);
      }
      if(type == float[].class){
         return Arrays.equals((float[])value, (float[])oValue);
      }
      if(type == int[].class){
         return Arrays.equals((int[])value, (int[])oValue);
      }
      if(type == long[].class){
         return Arrays.equals((long[])value, (long[])oValue);
      }
      if(type == short[].class){
         return Arrays.equals((short[])value, (short[])oValue);
      }
      if(type == boolean[].class){
         return Arrays.equals((boolean[])value, (boolean[])oValue);
      }
      return Arrays.equals((Object[])value, (Object[])oValue);
   }

   public static int hashCode(final Object value){
      if(value == null){
         return 0;
      }
      final Class<?> type = value.getClass();
      if(!type.isArray()){
         return value.hashCode();
      }
      if(type == byte[].class){
         return Arrays.hashCode((byte[])value);
      }
      if(type == char[].class){
         return Arrays.hashCode((char[])value);
      }
      if(type == double[].class){
         return Arrays.hashCode((double[])value);
      }
      if(type == float[].class){
         return Arrays.hashCode((float[])value);
      }
      if(type == int[].class){
         return Arrays.hashCode((int[])value);
      }
      if(type == long[].class){
         return Arrays.hashCode((long[])value);
      }
      if(type == short[].class){
         return Arrays.hashCode((short[])value);
      }
      if(type == boolean[].class){
         return Arrays.hashCode((boolean[])value);
      }
      return Arrays.hashCode((Object[])value);
   }

   public static String toString(final Object value){
      if(value == null){
         return "null";
      }
      final Class<?> type = value.getClass();
      if(!type.isArray()){
         return value.toString();
      }
      if(type == byte[].class){
         return Arrays.toString((byte[])value);
      }
      if(type == char[].class){
         return Arrays.toString((char[])value);
      }
      if(type == double[].class){
         return Arrays.toString((double[])value);
      }
      if(type == float[].class){
         return Arrays.toString((float[])value);
      }
      if(type == int[].class){
         return Arrays.toString((int[])value);
      }
      if(type == long[].class){
         return Arrays.toString((long[])value);
      }
      if(type == short[].class){
         return Arrays.toString((short[])value);
      }
      if(type == boolean[].class){
         return Arrays.toString((boolean[])value);
      }
      return Arrays.toString((Object[])value);
   }

   private ObjectUtils(){
   }
}
