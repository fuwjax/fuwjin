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
 * Sample subclass which has no declared members
 * @author michaeldoberenz
 */
public class SimpleGrandChild extends SimpleChild {
   /**
    * Creates a new instance
    */
   public SimpleGrandChild() {
      super(82.24, 134, "bland"); //$NON-NLS-1$
   }

   /**
    * Creates a subclassed instance
    * @param d the parent's s
    * @param i the grandparent's io
    * @param string the grandparent's s
    */
   public SimpleGrandChild(final double d, final int i, final String string) {
      super(d, i, string);
   }
}
