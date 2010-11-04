/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.pogo;

import static org.fuwjin.pogo.CodePointStreamFactory.streamBytes;

import org.fuwjin.complier.RuntimeClassLoader;

/**
 * A code generation class loader that builds a class from a pogo grammar.
 */
public class PogoClassLoader {
   public static class QualifiedGenerator {
      private final String packageName;
      private final String name;
      private final Object generator;
      private final String qualifiedName;

      /**
       * Creates a new instance.
       * @param qualifiedName the qualified class name of the generated source
       * @param generator the grammar to encode
       */
      public QualifiedGenerator(final String qualifiedName, final Object generator) {
         this.generator = generator;
         this.qualifiedName = qualifiedName;
         final int index = qualifiedName.lastIndexOf('.');
         packageName = qualifiedName.substring(0, index);
         name = qualifiedName.substring(index + 1);
      }

      @Override
      public String toString() {
         return packageName + '.' + name + '=' + generator;
      }
   }

   private final Grammar grammar;
   private final RuntimeClassLoader loader = new RuntimeClassLoader();

   /**
    * Creates a new instance.
    * @param pogoFile the grammar file
    */
   public PogoClassLoader(final String pogoFile) {
      try {
         grammar = Grammar.readGrammar(streamBytes(pogoFile));
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }

   public Class<?> loadClass(final QualifiedGenerator generator) throws ClassNotFoundException {
      return loadClass(generator.qualifiedName, generator);
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
