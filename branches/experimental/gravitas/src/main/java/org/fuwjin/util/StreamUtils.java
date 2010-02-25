package org.fuwjin.util;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.Thread.currentThread;

import java.io.FileNotFoundException;
import java.io.InputStream;

public final class StreamUtils{
   static{
      new StreamUtils();
   }

   public static InputStream load(final String path, final ClassLoader... loaders) throws FileNotFoundException{
      for(final ClassLoader loader: loaders){
         final InputStream stream = loader.getResourceAsStream(path);
         if(stream != null){
            return stream;
         }
      }
      throw new FileNotFoundException();
   }

   public static InputStream open(final String path, final Class<?>... classes) throws FileNotFoundException{
      final ClassLoader[] loaders = new ClassLoader[classes.length + 2];
      loaders[0] = getSystemClassLoader();
      loaders[1] = currentThread().getContextClassLoader();
      for(int i = 0; i < classes.length; i++){
         loaders[i + 2] = classes[i].getClassLoader();
      }
      return load(path, loaders);
   }

   private StreamUtils(){
   }
}
