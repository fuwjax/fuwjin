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

import org.fuwjin.dinah.Adapter;
import org.fuwjin.grin.env.Trace;

/**
 * Represents a value based accept statement.
 */
public class ValueAcceptStatement implements Expression {
   private final Expression value;
   private final boolean isNot;

   /**
    * Creates a new instance.
    * @param isNot true if the result should be negated
    * @param value the value to match
    */
   public ValueAcceptStatement(final boolean isNot, final Expression value) {
      this.isNot = isNot;
      this.value = value;
   }

   /**
    * Returns true if the statement is negated.
    * @return true if negated, false otherwise
    */
   public boolean isNot() {
      return isNot;
   }

   @Override
   public Object resolve(final Trace trace) throws AbortedException, ResolveException {
      if(value.equals(Variable.NEXT)) {
         trace.accept();
         return Adapter.UNSET;
      }
      final Object result = value.resolve(trace);
      final String str = String.valueOf(result);
      if(isNot) {
         try {
            trace.resolveAndRevert(new Expression() {
               @Override
               public Object resolve(final Trace trace) throws AbortedException, ResolveException {
                  int codePoint;
                  for(int index = 0; index < str.length(); index += Character.charCount(codePoint)) {
                     codePoint = str.codePointAt(index);
                     if(codePoint != trace.next()) {
                        throw trace.fail("failed while matching %s", str);
                     }
                     trace.accept();
                  }
                  return Adapter.UNSET;
               }
            });
         } catch(final ResolveException e) {
            trace.accept();
            return Adapter.UNSET;
         }
         throw trace.fail("failed while matching %s", str);
      }
      int codePoint;
      for(int index = 0; index < str.length(); index += Character.charCount(codePoint)) {
         codePoint = str.codePointAt(index);
         if(codePoint != trace.next()) {
            throw trace.fail("failed while matching %s", str);
         }
         trace.accept();
      }
      return Adapter.UNSET;
   }

   @Override
   public String toString() {
      return "accept " + (isNot ? "not " : "") + value;
   }

   /**
    * Returns the filter value.
    * @return the filter
    */
   public Expression value() {
      return value;
   }
}
