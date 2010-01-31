package org.fuwjin.jon;

import java.util.HashMap;
import java.util.Map;

public class JonLiteral {
   private static Map<String, String> primitives = new HashMap<String, String>();
   static {
      primitives.put("boolean", "Z");
      primitives.put("byte", "B");
      primitives.put("char", "C");
      primitives.put("double", "D");
      primitives.put("float", "F");
      primitives.put("int", "I");
      primitives.put("long", "J");
      primitives.put("short", "S");
   }

   public static String escape(final Object value) {
      return value.toString().replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"").replace("\t", "\\t");
   }

   public static Class<?> forName(final String value) {
      try {
         if(value.charAt(value.length() - 1) == ']') {
            return forArray(value);
         }
         return Class.forName(value);
      } catch(final ClassNotFoundException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public static String getName(final Class<?> cls) {
      if(cls.isArray()) {
         return getArrayName(cls.getComponentType()) + "[]";
      }
      return cls.getName();
   }

   public static char newLine(final String value) {
      assert "n".equals(value);
      return '\n';
   }

   public static Object nullValue(final String value) {
      assert "null".equals(value);
      return null;
   }

   public static char tab(final String value) {
      assert "t".equals(value);
      return '\t';
   }

   private static String binaryName(final String componentName) {
      final String name = primitives.get(componentName);
      if(name != null) {
         return name.toString();
      }
      return 'L' + componentName + ';';
   }

   private static Class<?> forArray(final String className) throws ClassNotFoundException {
      final StringBuilder builder = new StringBuilder();
      int bracket = className.indexOf('[');
      final String componentName = className.substring(0, bracket);
      final String binaryName = binaryName(componentName);
      do {
         builder.append('[');
         bracket = className.indexOf('[', bracket + 1);
      } while(bracket >= 0);
      return Class.forName(builder.append(binaryName).toString());
   }

   private static String getArrayName(final Class<?> cls) {
      if(cls.isArray()) {
         return getArrayName(cls.getComponentType()) + "[]";
      }
      return cls.getName();
   }
}
