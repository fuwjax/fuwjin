package org.fuwjin.gravitas.util;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.Thread.currentThread;

import java.io.InputStream;

public class StreamUtils{
   public static InputStream open(String path, Class<?>...classes){
      ClassLoader[] loaders = new ClassLoader[classes.length+2];
      loaders[0] = getSystemClassLoader();
      loaders[1] = currentThread().getContextClassLoader();
      for(int i=0;i<classes.length;i++){
         loaders[i+2] = classes[i].getClassLoader();
      }
      return open(path, loaders);
   }

   public static InputStream open(String path, ClassLoader[] loaders){
      for(ClassLoader loader: loaders){
         InputStream stream = loader.getResourceAsStream(path);
         if(stream != null){
            return stream;
         }
      }
      return null;
   }
}
