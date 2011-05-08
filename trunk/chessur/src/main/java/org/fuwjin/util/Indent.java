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
package org.fuwjin.util;

import java.util.Arrays;

/**
 * Simple class for managing indention during serialization.
 */
public class Indent {
   private static char[] spaces = new char[100];
   static {
      Arrays.fill(spaces, ' ');
      spaces[0] = '\n';
   }
   private int width = 3;

   /**
    * Decreases the indention.
    */
   public void decrease() {
      width -= 2;
   }

   /**
    * Increases the indention.
    */
   public void increase() {
      width += 2;
   }

   @Override
   public String toString() {
      return new String(spaces, 0, width);
   }
}
