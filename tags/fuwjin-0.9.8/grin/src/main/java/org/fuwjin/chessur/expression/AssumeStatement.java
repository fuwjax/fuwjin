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

import java.lang.reflect.Array;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.Trace;

/**
 * Allows testing for default values.
 */
public class AssumeStatement implements Expression {
   private static boolean isDefault(final Object value) {
      if(value == null) {
         return true;
      }
      if(value instanceof Boolean) {
         return !(Boolean)value;
      }
      if(value instanceof Number) {
         return ((Number)value).doubleValue() == 0;
      }
      if(value instanceof String) {
         return ((String)value).length() == 0;
      }
      if(value.getClass().isArray()) {
         return Array.getLength(value) == 0;
      }
      return false;
   }

   private final boolean isNot;
   private final Expression value;

   /**
    * Creates a new instance.
    * @param isNot true if the failure should be reversed
    * @param value the value to test for default equality
    */
   public AssumeStatement(final boolean isNot, final Expression value) {
      this.isNot = isNot;
      this.value = value;
   }

   /**
    * Returns true if this statement has been negated.
    * @return true if negated, false otherwise
    */
   public boolean isNot() {
      return isNot;
   }

   @Override
   public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
         throws AbortedException, ResolveException {
      if(isNot) {
         final Object result;
         try {
            result = trace.resolveAndRevert(value);
         } catch(final ResolveException e) {
            return Adapter.UNSET;
         }
         if(isDefault(result)) {
            return Adapter.UNSET;
         }
         throw trace.fail("unexpected value");
      }
      final Object result = trace.resolveAndRevert(value);
      if(isDefault(result)) {
         throw trace.fail("Unexpected default");
      }
      return Adapter.UNSET;
   }

   @Override
   public String toString() {
      return "assume " + (isNot ? "not " : "") + value;
   }

   /**
    * Returns the value or statement being tested.
    * @return the tested expression
    */
   public Expression value() {
      return value;
   }
}
