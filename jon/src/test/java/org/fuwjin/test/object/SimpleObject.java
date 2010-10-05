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
 * A sample serializable object for the JON tests.
 * @author michaeldoberenz
 */
public class SimpleObject {
   private final String s;
   private final IntegerObject io;

   /**
    * Creates a default simple object
    */
   public SimpleObject() {
      s = "howdy"; //$NON-NLS-1$
      io = new IntegerObject(17);
   }

   /**
    * Constructor for more complex objects
    * @param string the value of the string member
    * @param child the value of the integer object member
    */
   public SimpleObject(final String string, final IntegerObject child) {
      s = string;
      io = child;
   }

   /**
    * Constructor for child objects
    * @param i the internal value of the integer object memeber
    * @param string the value of the string member
    */
   protected SimpleObject(final int i, final String string) {
      s = string;
      io = new IntegerObject(i);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final SimpleObject o = (SimpleObject)obj;
         return s == null ? o.s == null : s.equals(o.s) && io == null ? o.io == null : io.equals(o.io);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      try {
         return io.hashCode() + s.hashCode();
      } catch(final RuntimeException e) {
         return super.hashCode();
      }
   }
}
