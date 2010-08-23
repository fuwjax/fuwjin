package org.fuwjin.complier;

import static org.fuwjin.pogo.CodePointStreamFactory.stream;

import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.PogoGrammar;
import org.fuwjin.pogo.LiteratePogo;

public class PogoClassLoader {
   private final Grammar grammar;
   private final RuntimeClassLoader loader = new RuntimeClassLoader();

   public PogoClassLoader(final String pogoFile) {
      try {
         grammar = PogoGrammar.readGrammar(stream(pogoFile));
      } catch(final Exception e) {
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
