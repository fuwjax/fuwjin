/*
 * This file is part of JON.
 *
 * JON is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * JON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2007 Michael Doberenz
 */
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
