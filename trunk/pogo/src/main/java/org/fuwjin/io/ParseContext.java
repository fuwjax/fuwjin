/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.io;

import static java.lang.Character.isHighSurrogate;
import static java.lang.Character.toChars;
import static java.lang.Character.toCodePoint;
import static java.lang.String.copyValueOf;

/**
 * The standard PogoContext for parsing. The CharSequence input should implement
 * the contract as specified by the CharSequence interface. However not all of
 * the contract constraints are necessary. In particular, this class expects the
 * following relaxed contract from a CharSequence implementation:
 * <p>
 * If the last successful call to charAt took an index=x, then
 * <ul>
 * <li>any subsequent call to charAt(x) should return the same character</li>
 * <li>the next call to charAt will take an index <= x+1</li>
 * <li>any subsequent call to subSequence(s,x+1) should be identical to
 * subSequence(s,x) concatenated with charAt(x) for any s <= x</li>
 * <li>a call to subSequence will take start <= end <= x+1</li>
 * </ul>
 * If the next call to charAt was unsuccessful at index=x+1, i.e. resulted in an
 * IndexOutOfBoundsException thrown, then any call to charAt(y) where y>=x
 * should result in an IndexOutOfBoundsException thrown. Note that in particular
 * the length and toString methods are not used here and therefore may be
 * adjusted to need. In particular, wrapping a suitably buffered InputStream
 * with a CharSequence is acceptable.
 */
public class ParseContext extends RootContext {
   private static String toCharString(final int ch) {
      return copyValueOf(toChars(ch));
   }

   private static String toRange(final int start, final int end) {
      return "[" + toCharString(start) + "-" + toCharString(end) + "]";
   }

   private static String toString(final int ch) {
      return ch == -1 ? "EOF" : "'" + toCharString(ch) + "'";
   }

   private int position;
   private final CharSequence input;
   private int line = 1;
   private int column = 1;

   /**
    * Creates a new instance.
    * @param input the input stream
    * @param object the initial value of the result object, may be null
    */
   public ParseContext(final CharSequence input) {
      this.input = input;
   }

   @Override
   public void accept(final PogoContext context) {
      final int c = read();
      PogoException ret = null;
      if(c == -1) {
         ret = context.failedCheck("EOF expecting any character");
      }
      success(ret);
   }

   @Override
   public void accept(final PogoContext context, final int ch) {
      final Position mark = position();
      final int c = read();
      PogoException ret = null;
      if(c != ch) {
         seek(mark);
         ret = context.failedCheck(toString(c) + " expecting " + toString(ch));
      }
      success(ret);
   }

   @Override
   public void accept(final PogoContext context, final int start, final int end) {
      final Position mark = position();
      final int c = read();
      PogoException ret = null;
      if(c < start || c > end) {
         seek(mark);
         ret = context.failedCheck(toString(c) + " expecting " + toRange(start, end));
      }
      success(ret);
   }

   @Override
   public boolean hasRemaining() {
      try {
         input.charAt(position);
         return true;
      } catch(final IndexOutOfBoundsException e) {
         return false;
      }
   }

   @Override
   public PogoContext newChild(final String name) {
      return new ChildContext(name, this);
   }

   @Override
   public Position position() {
      return new Position(position, line, column);
   }

   private int read() {
      try {
         final char c1 = input.charAt(position);
         if(c1 == '\n') {
            line++;
            column = 1;
         } else {
            column++;
         }
         position++;
         if(isHighSurrogate(c1)) {
            final char c2 = input.charAt(position);
            position++;
            success(null);
            return toCodePoint(c1, c2);
         }
         success(null);
         return c1;
      } catch(final IndexOutOfBoundsException e) {
         return -1;
      }
   }

   @Override
   public void seek(final Position mark) {
      position = mark.position();
      line = mark.line();
      column = mark.column();
   }

   @Override
   public String substring(final int mark) {
      return input.subSequence(mark, position).toString();
   }
}
