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
 * A class that references it's own class
 * @author michaeldoberenz
 */
public class ClassRef {
   private final Class<?> cls;

   /**
    * Creates a new instance
    */
   public ClassRef() {
      cls = ClassRef.class;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ClassRef o = (ClassRef)obj;
         return cls.equals(o.cls);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return cls.hashCode();
   }
}
