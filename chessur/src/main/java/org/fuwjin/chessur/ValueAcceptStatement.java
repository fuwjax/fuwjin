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

   @Override
   public String toString() {
      return "accept " + (isNot ? "not " : "") + value;
   }

   @Override
   public State transform(final State state) {
      State result = value.transform(state);
      if(!result.isSuccess()) {
         return result;
      }
      final String str;
      if(result.value() instanceof Integer) {
         str = new String(Character.toChars((Integer)result.value()));
      } else {
         str = String.valueOf(result.value());
      }
      int codePoint;
      for(int index = 0; index < str.length(); index += Character.charCount(codePoint)) {
         codePoint = str.codePointAt(index);
         if(codePoint != result.current()) {
            if(isNot) {
               return state.accept();
            }
            return state.failure(result.failure("unexpected character"), "failed while matching %s", str);
         }
         result = result.accept();
      }
      if(isNot) {
         return state.failure(result.failure("unexpected match"), "failed while matching %s", str);
      }
      return result;
   }
}
