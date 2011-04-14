/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.fuwjin.util.TypeUtils;

/**
 * Function encapsulating the "instanceof" keyword.
 */
public class InstanceOfFunction extends FixedArgsFunction {
   private final Type type;

   /**
    * Creates a new instance.
    * @param category the function category
    * @param type the expected type
    */
   public InstanceOfFunction(final String category, final Type type) {
      super(category + ".instanceof", new Type[1]);
      this.type = type;
   }

   @Override
   public Object invokeSafe(final Object... args) {
      return TypeUtils.isInstance(type, args[0]);
   }

   @Override
   protected Member member() {
      return null;
   }
}
