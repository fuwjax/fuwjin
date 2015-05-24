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
 * Marks a position during a pogo operation.
 */
public interface PogoPosition {
   /**
    * The null position.
    */
   PogoPosition NULL = new PogoPosition() {
      @Override
      public void release() {
         // do nothing
      }

      @Override
      public void reset() {
         // do nothing
      }

      @Override
      public String toString() {
         return null;
      }
   };

   /**
    * Releases the position.
    */
   void release();

   /**
    * Resets the state to this position.
    */
   void reset();

   /**
    * Returns the line/column number normally, but in the case of a
    * {@link PogoState#buffer(boolean)}, returns the buffered string.
    * @return the line/column or the matched string.
    */
   @Override
   String toString();
}
