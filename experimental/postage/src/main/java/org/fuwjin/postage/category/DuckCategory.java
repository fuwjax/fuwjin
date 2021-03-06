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
package org.fuwjin.postage.category;

import org.fuwjin.postage.CompositeFunction;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.function.DuckFunction;

/**
 * Manages functions that can reflectively point to any object.
 */
public class DuckCategory extends AbstractCategory {
   /**
    * Creates a new instance. Though the prefix can be any string, it is
    * suggested that to avoid conflicting with normal java classes, that
    * keywords such as volatile or true be used.
    * @param prefix the prefix for functions generated by this factory
    */
   public DuckCategory(final String prefix) {
      super(prefix);
   }

   @Override
   public Function getTargetFunction(final String name) {
      return new CompositeFunction(name, new DuckFunction(name));
   }
}
