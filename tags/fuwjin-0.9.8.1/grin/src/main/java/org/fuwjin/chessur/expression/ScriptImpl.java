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
package org.fuwjin.chessur.expression;

import org.fuwjin.grin.env.Trace;

/**
 * Represents a Specificaton reference.
 */
public class ScriptImpl extends Executable implements Expression {
   private Declaration decl;
   private final String name;

   /**
    * Creates a new instance.
    * @param name the name of the script
    */
   public ScriptImpl(final String name) {
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
   @Override
   public String name() {
      return name;
   }

   @Override
   public Object resolve(final Trace trace)
         throws AbortedException, ResolveException {
      return expression().resolve(trace);
   }

   @Override
   public String toString() {
      return "<" + name() + ">";
   }

   @Override
   protected Expression expression() {
      if(decl == null) {
         throw new IllegalStateException("Undefined script " + name);
      }
      return decl;
   }

   /**
    * Sets this specification's declaration.
    * @param decl the new declaration
    */
   protected void setDecl(final Declaration decl) {
      this.decl = decl;
   }
}
