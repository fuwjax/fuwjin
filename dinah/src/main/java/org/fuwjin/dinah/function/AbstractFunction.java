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
package org.fuwjin.dinah.function;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;

/**
 * Base implementation of Function interface.
 */
public abstract class AbstractFunction implements Function {
   /**
    * The null Function.
    */
   public static final AbstractFunction NULL = new UnsupportedFunction(null);
   private final String name;
   private final Type[] argTypes;

   protected AbstractFunction(final String name, final Type... argTypes) {
      this.name = name;
      this.argTypes = argTypes;
   }

   /**
    * Creates a composite of this function and the next function. May alter this
    * function. This method is an implementation detail and should not be called
    * externally.
    * @param next the next function in the chain
    * @return the new composite function
    */
   public final AbstractFunction join(final AbstractFunction next) {
      if(AbstractFunction.NULL.equals(next)) {
         return this;
      }
      return joinImpl(next);
   }

   @Override
   public final String name() {
      return name;
   }

   /**
    * Restricts the function to just the signature. Does not alter this
    * function. This method is an implementation detail and should not be called
    * externally.
    * @param signature the signature to restrict to
    * @return the new restricted function, or null if this function cannot
    *         handle the signature
    */
   public abstract AbstractFunction restrict(FunctionSignature signature);

   @Override
   public String toString() {
      return getClass().getSimpleName() + ": " + name();
   }

   protected int argCount() {
      return argTypes.length;
   }

   protected Type argType(final int index) {
      return argTypes()[index];
   }

   protected Type[] argTypes() {
      return argTypes;
   }

   protected boolean isPrivate() {
      return true;
   }

   protected AbstractFunction joinImpl(final AbstractFunction next) {
      return new CompositeFunction(this, next);
   }
}
