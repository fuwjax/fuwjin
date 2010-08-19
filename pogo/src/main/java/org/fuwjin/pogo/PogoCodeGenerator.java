/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import static org.fuwjin.pogo.PredefinedGrammar.PogoCodeSerial;

import org.fuwjin.io.PogoException;

/**
 * The wrapper object for generating the code from a pogo grammar.
 */
public class PogoCodeGenerator {
   private final String qualifiedPackage;
   private final String name;
   private final Grammar grammar;

   /**
    * Creates a new instance.
    * @param qualifiedName the qualified class name of the generated source
    * @param grammar the grammar to encode
    */
   public PogoCodeGenerator(final String qualifiedName, final Grammar grammar) {
      this.grammar = grammar;
      final int index = qualifiedName.lastIndexOf('.');
      qualifiedPackage = qualifiedName.substring(0, index);
      name = qualifiedName.substring(index + 1);
   }

   /**
    * Returns the source code.
    * @return the source code
    * @throws PogoException
    */
   public String toCode() throws PogoException {
      final StringBuilder builder = new StringBuilder();
      PogoCodeSerial.get().serial(this, builder);
      return builder.toString();
   }

   @Override
   public String toString() {
      return qualifiedPackage + '.' + name + '=' + grammar;
   }
}
