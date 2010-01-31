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
package org.fuwjin.jon;

public class Reference {
   private String name;
   private Object value;

   public Reference(final Object value) {
      this.value = value;
   }

   public Reference(final String name, final Object value) {
      this.name = name;
      this.value = value;
   }

   public String name() {
      return name;
   }

   public Object value() {
      return value;
   }
}