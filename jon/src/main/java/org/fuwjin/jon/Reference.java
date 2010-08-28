/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

public class Reference {
   private String name;
   private Object value;

   public Reference() {
      // do nothing
   }

   public Reference(final Object value) {
      this.value = value;
   }

   public String name() {
      return name;
   }

   protected void name(final String name) {
      assert this.name == null;
      this.name = name;
   }

   public Object value() {
      return value;
   }

   protected void value(final Object value) {
      assert this.value == null;
      this.value = value;
   }
}