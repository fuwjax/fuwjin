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

/**
 * Test class with a non-static inner class member
 * @author michaeldoberenz
 */
public class InnerChild {
   /**
    * Non static class with both a synthetic and real field
    * @author michaeldoberenz
    */
   public class Child {
      private final int i;

      /**
       * Creates a new instance
       */
      public Child() {
         i = 1;
      }

      @Override
      public boolean equals(final Object obj) {
         try {
            final Child o = (Child)obj;
            return o.i == i;
         } catch(final RuntimeException e) {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return i;
      }
   }

   private final Child inner;

   /**
    * Creates a new instance
    */
   public InnerChild() {
      inner = new Child();
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final InnerChild o = (InnerChild)obj;
         return inner.equals(o.inner);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   /**
    * Returns the inner child.
    * @return the inner child
    */
   public Child getChild() {
      return inner;
   }

   @Override
   public int hashCode() {
      return inner.hashCode();
   }
}
