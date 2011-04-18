/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur;

/**
 * Represents a Specificaton reference.
 */
public class Script extends Transformer {
   private Declaration decl;
   private final String name;

   public Script(final String name) {
      this.name = name;
   }

   /**
    * Returns the declaration.
    * @return the declaration
    */
   public Declaration declaration() {
      return decl;
   }

   /**
    * Returns the name.
    * @return the name
    */
   public String name() {
      return name;
   }

   @Override
   public String toString() {
      return "<" + name() + ">";
   }

   @Override
   public State transform(final State state) {
      if(decl == null) {
         throw new RuntimeException("Undefined script " + name);
      }
      return decl.transform(state);
   }

   /**
    * Sets this specification's declaration.
    * @param decl the new declaration
    */
   protected void setDecl(final Declaration decl) {
      this.decl = decl;
   }
}
