package org.fuwjin.compiler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.fuwjin.dinah.function.AbstractFunction;

public class VirtualClass {
   public static void addModifier(final Set<String> modifiers, final String modifier) {
      modifiers.add(modifier);
   }

   public static void addParameter(final List<String> parameters, final String parameter) {
      parameters.add(parameter);
   }

   public static boolean isStatic(final Set<String> modifiers) {
      return modifiers.contains("static");
   }

   public static Set<String> newModifiers() {
      return new HashSet<String>();
   }

   public static List<String> newParameterList() {
      return new ArrayList<String>();
   }

   private final Map<String, String> imports = new HashMap<String, String>();
   private final List<String> demands = new ArrayList<String>();

   public void addConstructor(final Set<String> modifiers, final String declaringClass, final List<String> types) {
   }

   public void addField(final Set<String> modifiers, final String declaringClass, final String name, final String type) {
   }

   public void addImport(final String name, final boolean isStatic, final boolean isOnDemand) {
      if(!isStatic) {
         if(isOnDemand) {
            demands.add(name);
         } else {
            imports.put(name.substring(name.lastIndexOf('.')), name);
         }
      }
   }

   public void addMethod(final Set<String> modifiers, final String declaringClass, final String name,
         final List<String> types) {
   }

   public InvocationTargetException exception() {
      throw new AssertionError("virtual methods cannot be invoked");
   }

   public Iterable<? extends AbstractFunction> functions() {
      return null;
   }
}
