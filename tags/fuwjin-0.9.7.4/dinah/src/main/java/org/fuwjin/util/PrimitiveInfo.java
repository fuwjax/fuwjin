package org.fuwjin.util;

public enum PrimitiveInfo {
   BOOL(boolean.class, Boolean.class, "Z"), CHAR(char.class, Character.class, "C"), BYTE(byte.class, Byte.class, "B"),
   SHORT(short.class, Short.class, "S"), INT(int.class, Integer.class, "I"), LONG(long.class, Long.class, "J"), FLOAT(
         float.class, Float.class, "F"), DOUBLE(double.class, Double.class, "D");
   public static PrimitiveInfo byClass(final Class<?> cls) {
      for(final PrimitiveInfo info: values()) {
         if(info.primitive().equals(cls) || info.wrapper().equals(cls)) {
            return info;
         }
      }
      return null;
   }

   public static PrimitiveInfo byPrimitiveName(final String name) {
      for(final PrimitiveInfo info: values()) {
         if(info.primitive().getName().equals(name)) {
            return info;
         }
      }
      return null;
   }

   private final Class<?> primitive;
   private final String shortName;
   private final Class<?> wrapper;

   private PrimitiveInfo(final Class<?> primitive, final Class<?> wrapper, final String shortName) {
      this.primitive = primitive;
      this.wrapper = wrapper;
      this.shortName = shortName;
   }

   public Class<?> primitive() {
      return primitive;
   }

   public String shortName() {
      return shortName;
   }

   public Class<?> wrapper() {
      return wrapper;
   }
}
