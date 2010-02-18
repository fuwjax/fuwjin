package org.fuwjin.gravitas.config;

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
      try{
         return Class.forName(type);
      }catch(final ClassNotFoundException e){
         throw new RuntimeException("Could not locate " + type + ". Add a \"use\" statement to context.gravitas.");
      }
   }

   public boolean contains(String name){
      return packages.contains(name);
   }
}
