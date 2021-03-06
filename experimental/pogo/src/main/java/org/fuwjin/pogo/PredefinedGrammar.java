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
import static org.fuwjin.pogo.Grammar.readGrammar;

import org.fuwjin.pogo.PogoClassLoader.QualifiedGenerator;

/**
 * The set of predefined grammars from the pogo package.
 */
public enum PredefinedGrammar {
   /**
    * The default pogo parser.
    */
   PogoParse("pogoParse.pogo"), //$NON-NLS-1$
   /**
    * The default pogo serializer.
    */
   PogoSerial("pogoSerial.pogo"), //$NON-NLS-1$
   /**
    * The default pogo code serializer.
    */
   PogoCodeSerial("pogoCodeSerial.pogo"); //$NON-NLS-1$
   /**
    * Serializes a grammar to generated code.
    * @param qualifiedName the name of the generated class
    * @param grammar the grammar to serialize
    * @return the serialized grammar
    * @throws PogoException if the serialization fails
    */
   public static String grammarToJava(final String qualifiedName, final Grammar grammar) throws PogoException {
      return PogoCodeSerial.grammar().toString(new QualifiedGenerator(qualifiedName, grammar));
   }

   /**
    * Serializes the input grammar to a string.
    * @param grammar the grammar to serialize
    * @return the serialized grammar
    * @throws PogoException if the parse fails
    */
   public static String grammarToPogo(final Grammar grammar) throws PogoException {
      return PogoSerial.grammar().toString(grammar);
   }

   private final String file;
   private Grammar grammar;

   private PredefinedGrammar(final String file) {
      this.file = file;
   }

   /**
    * Returns the grammar
    * @return the grammar
    */
   public Grammar grammar() {
      if(grammar == null) {
         try {
            grammar = readGrammar(streamBytes(file));
         } catch(final Exception e) {
            throw new RuntimeException(e);
         }
      }
      return grammar;
   }
}
