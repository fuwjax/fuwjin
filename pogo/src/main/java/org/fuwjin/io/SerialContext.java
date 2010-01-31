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
   public SerialContext(final Object object) {
      super(object);
      builder = new StringBuilder();
   }

   @Override
   public void accept() {
      success(true, null);
      builder.append(get());
   }

   @Override
   public void accept(final int ch) {
      success(true, null);
      builder.append(toChars(ch));
   }

   @Override
   public void accept(final int start, final int end) {
      success(true, null);
      // throw new UnsupportedOperationException();
   }

   @Override
   public boolean hasRemaining() {
      return true;
   }

   @Override
   public PogoContext newChild() {
      return new ChildSerialContext(this);
   }

   @Override
   public int position() {
      return builder.length();
   }

   @Override
   public void seek(final int mark) {
      builder.setLength(mark);
   }

   @Override
   public String substring(final int mark) {
      return builder.substring(mark);
   }
}
