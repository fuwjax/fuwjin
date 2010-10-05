/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.pogo.state;

import org.fuwjin.pogo.CodePointStream;

/**
 * The state of a parse.
 */
public class ParseState extends AbstractState {
   private int buffers;
   private final StringBuilder builder = new StringBuilder();
   private final CodePointStream stream;

   /**
    * Creates a new instance.
    * @param stream the stream to parse
    */
   public ParseState(final CodePointStream stream) {
      this.stream = stream;
      super.set(new ParsePosition(this, stream.next()));
   }

   @Override
   public boolean advance(final int start, final int end) {
      final int ch = current().codePoint();
      if(ch < start || ch > end) {
         fail(start, end);
         return false;
      }
      append(ch);
      PogoPosition next = current().next();
      if(next != null) {
         if(next == PogoPosition.NULL) {
            throw new IllegalStateException();
         } else if(!shouldBufferNext()) {
            current().clearNext();
         }
         ((ParsePosition)next).setStart(builder.length());
      } else {
         next = new ParsePosition(current(), shouldBufferNext(), builder.length(), stream.next());
      }
      super.set((ParsePosition)next);
      return true;
   }

   protected void append(final int codePoint) {
      if(buffers > 0) {
         builder.appendCodePoint(codePoint);
      }
   }

   private PogoPosition buffer() {
      final int start = builder.length();
      return new PogoPosition() {
         @Override
         public void release() {
            buffers--;
         }

         @Override
         public void reset() {
            builder.setLength(start);
         }

         @Override
         public String toString() {
            return builder.substring(start);
         }
      };
   }

   @Override
   public PogoPosition buffer(final boolean required) {
      if(required || buffers > 0) {
         if(buffers == 0) {
            builder.setLength(0);
            current().setStart(0);
         }
         buffers++;
         return buffer();
      }
      return PogoPosition.NULL;
   }

   @Override
   public ParsePosition current() {
      return (ParsePosition)super.current();
   }

   @Override
   protected void set(final AbstractPosition pos) {
      super.set(pos);
      builder.setLength(current().start());
   }
}
