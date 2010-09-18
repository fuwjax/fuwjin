/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages literals for JON.
 */
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

   private static String binaryName(final String componentName) {
      final String name = primitives.get(componentName);
      if(name != null) {
         return name.toString();
      }
      return 'L' + componentName + ';';
   }

   /**
    * Escapes the backslashes, line feeds, double quotes, and tabs.
    * @param value the value to escape
    * @return the escaped string
    */
   public static String escape(final Object value) {
      return value.toString().replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"").replace("\t", "\\t");
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

   /**
    * Returns a class instance for the name.
    * @param value the class name
    * @return the class instance
    */
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

   private static String getArrayName(final Class<?> cls) {
      if(cls.isArray()) {
         return getArrayName(cls.getComponentType()) + "[]";
      }
      return cls.getName();
   }

   /**
    * Returns the JON name for the class.
    * @param cls the class
    * @return the class name
    */
   public static String getName(final Class<?> cls) {
      if(cls.isArray()) {
         return getArrayName(cls.getComponentType()) + "[]";
      }
      return cls.getName();
   }

   /**
    * Returns the new line.
    * @return the new line
    */
   public static char newLine() {
      return '\n';
   }

   /**
    * Returns the tab.
    * @return the tab
    */
   public static char tab() {
      return '\t';
   }
}
