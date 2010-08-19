package org.fuwjin.complier;

import org.fuwjin.io.PogoException;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoUtils;

public class PogoClassLoader {
   private final Grammar grammar;
   private final RuntimeClassLoader loader = new RuntimeClassLoader();

   public PogoClassLoader(final String pogoFile) {
      try {
         grammar = PogoUtils.readGrammar(pogoFile);
      } catch(final PogoException e) {
         throw new RuntimeException(e);
      }
   }

   public Class<?> loadClass(final String name, final Object generator) throws ClassNotFoundException {
      try {
         return loader.loadClass(name);
      } catch(final ClassNotFoundException e) {
         try {
            final StringBuilder builder = new StringBuilder();
            grammar.serial(generator, builder);
            loader.compile(name, builder.toString());
            return loader.loadClass(name);
         } catch(final PogoException e1) {
            throw new ClassNotFoundException("Could not build class", e1);
         }
      }
   }
}
