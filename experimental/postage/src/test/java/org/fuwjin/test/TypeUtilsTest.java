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
package org.fuwjin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Type;

import org.fuwjin.postage.type.TypeUtils;
import org.junit.Test;

public class TypeUtilsTest {
   @Test
   public void testFailTwoClassUnion() {
      assertNull(TypeUtils.union(String.class, Long.class));
   }

   @Test
   public void testInterestingUnion() {
      final Type type = TypeUtils.union(CharSequence.class, Serializable.class);
      assertTrue(TypeUtils.isAssignableTo(type, CharSequence.class));
      assertTrue(TypeUtils.isAssignableTo(type, Serializable.class));
   }

   @Test
   public void testSimpleIntersection() {
      assertEquals(TypeUtils.intersect(String.class, String.class), String.class);
      assertEquals(TypeUtils.intersect(CharSequence.class, String.class), CharSequence.class);
      assertNull(TypeUtils.intersect(CharSequence.class, void.class));
      assertEquals(TypeUtils.intersect(void.class, void.class), void.class);
      assertNull(TypeUtils.union(CharSequence.class, null));
   }

   @Test
   public void testSimpleUnion() {
      assertEquals(TypeUtils.union(String.class, String.class), String.class);
      assertEquals(TypeUtils.union(CharSequence.class, String.class), String.class);
      assertNull(TypeUtils.union(CharSequence.class, void.class));
      assertEquals(TypeUtils.union(void.class, void.class), void.class);
      assertNull(TypeUtils.union(CharSequence.class, null));
   }
}
