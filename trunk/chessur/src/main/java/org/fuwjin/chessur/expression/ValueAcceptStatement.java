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

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;

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
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      final Object result = value.resolve(input, output, scope);
      final String str;
      if(result instanceof Integer) {
         str = new String(Character.toChars((Integer)result));
      } else {
         str = String.valueOf(result);
      }
      if(isNot) {
         final SourceStream sub = input.detach();
         int codePoint;
         for(int index = 0; index < str.length(); index += Character.charCount(codePoint)) {
            codePoint = str.codePointAt(index);
            int cp;
            try {
               cp = ((Integer)sub.read(snapshot).value()).intValue();
            } catch(final ResolveException e) {
               return input.read(snapshot).value();
            }
            if(codePoint != cp) {
               return input.read(snapshot).value();
            }
         }
         throw new ResolveException(snapshot, "failed while matching %s", str);
      }
      int codePoint;
      for(int index = 0; index < str.length(); index += Character.charCount(codePoint)) {
         codePoint = str.codePointAt(index);
         if(codePoint != ((Integer)input.read(snapshot).value()).intValue()) {
            throw new ResolveException(snapshot, "failed while matching %s", str);
         }
      }
      return str.codePointBefore(str.length());
   }

   @Override
   public String toString() {
      return "accept " + (isNot ? "not " : "") + value;
   }
}
