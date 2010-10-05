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
package org.fuwjin.test.object;

/**
 * Simple subclass that hides a parent field
 * @author michaeldoberenz
 */
public class SimpleChild extends SimpleObject {
   private final double s;

   /**
    * creates a default instance
    */
   public SimpleChild() {
      super(181, "curious"); //$NON-NLS-1$
      s = 12.234;
   }

   /**
    * constructor for subclasses
    * @param d the value of this instances s
    * @param i the parent io
    * @param string the parent s
    */
   protected SimpleChild(final double d, final int i, final String string) {
      super(i, string);
      s = d;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final SimpleChild o = (SimpleChild)obj;
         return s == o.s && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() + (int)Double.doubleToLongBits(s);
   }
}
