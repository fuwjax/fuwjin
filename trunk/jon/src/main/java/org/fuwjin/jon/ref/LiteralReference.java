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
package org.fuwjin.jon.ref;

public class LiteralReference extends BaseReference {
   private final Object value;

   public LiteralReference(final String name, final Object type, final Object value) {
      super(name, type);
      this.value = value;
   }

   public Object get() {
      return value;
   }
}
