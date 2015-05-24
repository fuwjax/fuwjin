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
package org.fuwjin.sample;

public class CollisionObject {
   // String someName()
   // void someName(String)
   public static String someName = "test";

   // Long someName(long l)
   static Long someName(final long l) {
      return l;
   }

   // int someName(String, int)
   static int someName(final String value, final int x) {
      return x;
   }

   // String someName(CollisionObject, String)
   String someName(final String t) {
      return t;
   }
}
