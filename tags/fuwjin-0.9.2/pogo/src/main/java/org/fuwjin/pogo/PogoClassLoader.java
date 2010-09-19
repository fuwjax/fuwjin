package org.fuwjin.pogo;

import static org.fuwjin.pogo.CodePointStreamFactory.streamBytes;

import org.fuwjin.complier.RuntimeClassLoader;

/**
 * A code generation class loader that builds a class from a pogo grammar.
 */
public class PogoClassLoader {
   private final Grammar grammar;
   private final RuntimeClassLoader loader = new RuntimeClassLoader();

   /**
    * Creates a new instance.
    * @param pogoFile the grammar file
    */
   public PogoClassLoader(final String pogoFile) {
      try {
         grammar = PogoGrammar.readGrammar(streamBytes(pogoFile));
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Loads a class. If the name does not already reference an existing class,
    * then the generator will be serialized according to the grammar file.
    * @param name the name of the class
    * @param generator the object to serialize into a class
    * @return the generated class
    * @throws ClassNotFoundException if the class could not be generated
    */
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
