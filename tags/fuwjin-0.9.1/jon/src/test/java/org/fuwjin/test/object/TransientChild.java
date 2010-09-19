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

import java.io.Serializable;

/**
 * A subclass of integer object with no serialized fields
 */
public class TransientChild extends IntegerObject implements Serializable {
   private static final long serialVersionUID = 1L;
   private transient final String alsoIgnored = "beta"; //$NON-NLS-1$

   /**
    * Creates a new instance
    */
   public TransientChild() {
      super(191);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final TransientChild o = (TransientChild)obj;
         return alsoIgnored.equals(o.alsoIgnored) && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() + alsoIgnored.hashCode();
   }
}
