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

/**
 * Manages the current position of a serial operation.
 */
public class SerialPosition extends AbstractPosition {
   private int start;

   /**
    * Creates a new instance.
    * @param pos the previous position
    * @param start the current buffer position
    * @param line the current line number
    * @param column the current column number
    */
   public SerialPosition(final SerialPosition pos, final int start, final int line, final int column) {
      super(pos, false, line, column);
      this.start = start;
   }

   /**
    * Creates a new initial instance.
    * @param state the pogo state
    */
   public SerialPosition(final SerialState state) {
      super(state);
      start = 0;
   }

   protected void setStart(final int start) {
      this.start = start;
   }

   protected int start() {
      return start;
   }
}
