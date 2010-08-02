/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.io;

import static java.lang.Character.toChars;

/**
 * The standard PogoContext for serialization.
 */
public class SerialContext extends RootContext {
   private final StringBuilder builder;

   /**
    * Creates a new instance.
    * @param object the object to serialize
    */
   public SerialContext() {
      builder = new StringBuilder();
   }

   @Override
   public void accept(final PogoContext context) {
      success(null);
      // throw new UnsupportedOperationException();
   }

   @Override
   public void accept(final PogoContext context, final int ch) {
      success(null);
      builder.append(toChars(ch));
   }

   @Override
   public void accept(final PogoContext context, final int start, final int end) {
      success(null);
      // throw new UnsupportedOperationException();
   }

   @Override
   public boolean hasRemaining() {
      return true;
   }

   @Override
   public PogoContext newChild(final String name) {
      return new ChildSerialContext(name, this);
   }

   @Override
   public Position position() {
      int line = 1;
      int col = 1;
      for(int i = 0; i < builder.length(); i++) {
         if(builder.charAt(i) == '\n') {
            line++;
            col = 1;
         } else {
            col++;
         }
      }
      return new Position(builder.length(), line, col);
   }

   @Override
   public void seek(final Position mark) {
      builder.setLength(mark.position());
   }

   @Override
   public String substring(final int mark) {
      return builder.substring(mark);
   }
}
