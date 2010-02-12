package org.fuwjin.gravitas.parser;

import java.util.LinkedList;
import java.util.List;

class ClassResolver{
   private final List<String> packages = new LinkedList<String>();

   void addPackage(final String packageName){
      packages.add(packageName);
   }

   Class<?> forName(final String type){
      for(final String pkg: packages){
         try{
            return Class.forName(pkg + '.' + type);
         }catch(final ClassNotFoundException e){
            // continue;
         }
      }
      throw new RuntimeException("Could not locate " + type + ". Add a \"use\" statement to context.gravitas.");
   }
}
