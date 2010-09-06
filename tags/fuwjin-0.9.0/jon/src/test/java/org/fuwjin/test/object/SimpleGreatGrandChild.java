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
package org.fuwjin.test.object;

import java.util.Arrays;

/**
 * 4th order nesting situation with hiding
 * @author michaeldoberenz
 */
public class SimpleGreatGrandChild extends SimpleGrandChild {
   private final String[] s;

   /**
    * Creates a default instance
    */
   public SimpleGreatGrandChild() {
      super(57.354, 235, "nestedness"); //$NON-NLS-1$
      s = new String[]{"crazy", "train"}; //$NON-NLS-1$ //$NON-NLS-2$
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final SimpleGreatGrandChild o = (SimpleGreatGrandChild)obj;
         return Arrays.equals(s, o.s) && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(s) + super.hashCode();
   }
}
