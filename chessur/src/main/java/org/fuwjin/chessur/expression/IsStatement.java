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
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.Adapter;

/**
 * Allows testing for default values.
 */
public class IsStatement implements Expression {
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
   public IsStatement(final boolean isNot, final Expression value) {
      this.isNot = isNot;
      this.value = value;
   }

   public boolean isNot() {
      return isNot;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws ResolveException, AbortedException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      if(isNot) {
         final Object result;
         try {
            result = snapshot.resolve(value, false);
         } catch(final ResolveException e) {
            return Adapter.UNSET;
         }
         if(isDefault(result)) {
            return Adapter.UNSET;
         }
         throw new ResolveException("unexpected value: %s", snapshot);
      }
      final Object result = snapshot.resolve(value, false);
      if(isDefault(result)) {
         throw new ResolveException("Unexpected default: %s", snapshot);
      }
      return Adapter.UNSET;
   }

   @Override
   public String toString() {
      return "is " + (isNot ? "not " : "") + value;
   }

   public Expression value() {
      return value;
   }
}
