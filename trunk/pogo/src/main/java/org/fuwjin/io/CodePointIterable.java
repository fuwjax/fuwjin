/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.io;

import static java.lang.Character.isHighSurrogate;
import static java.lang.Character.toCodePoint;

/**
 * Iterates over the code points of a {@link CharSequence}. This class is not
 * thread-safe and should not be shared across thread boundaries.
 */
public class CodePointIterable {
   private final CharSequence seq;
   private int position;

   /**
    * Creates a new instance.
    * @param seq the {@link CharSequence} to iterate over.
    */
   public CodePointIterable(final CharSequence seq) {
      this.seq = seq;
   }

   /**
    * Returns the next code point.
    * @return the next code point
    * @throws IndexOutOfBoundsException if there is no next codepoint
    */
   public int next() throws IndexOutOfBoundsException {
      final char c1 = seq.charAt(position);
      ++position;
      if(isHighSurrogate(c1)) {
         final char c2 = seq.charAt(position);
         ++position;
         return toCodePoint(c1, c2);
      }
      return c1;
   }

   /**
    * Returns the current position.
    * @return the current position
    */
   public int position() {
      return position;
   }

   /**
    * Seeks to an earlier position. The {@code mark} must be a position
    * previously reported by the position() method.
    * @param mark the new position
    */
   public void seek(final int mark) {
      assert mark >= 0 && mark <= position;
      position = mark;
   }

   /**
    * The substring from {@code mark} to the current position. The {@code mark}
    * must be a position previously reported by the position() method.
    * @param mark the start of the substring
    * @return the substring
    */
   public CharSequence substring(final int mark) {
      assert mark >= 0 && mark <= position;
      return seq.subSequence(mark, position);
   }
}
