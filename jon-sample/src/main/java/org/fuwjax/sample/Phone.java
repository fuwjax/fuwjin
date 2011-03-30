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
package org.fuwjax.sample;

/**
 * A hypothetical member bean for the model.
 */
public class Phone {
   /**
    * A sample enum.
    */
   public enum PhoneType {
      /**
       * a cell phone.
       */
      MOBILE,
      /**
       * a home phone.
       */
      HOME,
      /**
       * a work phone.
       */
      WORK
   }

   private final int areaCode;
   private final int block;
   private final int index;

   /**
    * Creates a new instance.
    * @param areaCode the area code
    * @param block the block number
    * @param index the index number
    */
   public Phone(final int areaCode, final int block, final int index) {
      this.areaCode = areaCode;
      this.block = block;
      this.index = index;
   }

   /**
    * Returns the area code.
    * @return the area code
    */
   public int getAreaCode() {
      return areaCode;
   }

   /**
    * Returns the block number.
    * @return the block number
    */
   public int getBlock() {
      return block;
   }

   /**
    * Returns the index number.
    * @return the index number
    */
   public int getIndex() {
      return index;
   }

   @Override
   public String toString() {
      return String.format("%3d-%3d-%4d", areaCode, block, index); //$NON-NLS-1$
   }
}