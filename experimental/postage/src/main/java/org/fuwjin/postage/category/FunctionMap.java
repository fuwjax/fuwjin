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

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.postage.CompositeFunction;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionTarget;

/**
 * A map for functions. Multiple functions may be stored in one name, and will
 * combine into a single CompositeFunction.
 */
public class FunctionMap {
   private final Map<String, CompositeFunction> functions = new HashMap<String, CompositeFunction>();

   /**
    * Returns the function stored as the name, or null if no such function
    * exists.
    * @param name the name of the function
    * @return the function, or null if it does not exist
    */
   public Function get(final String name) {
      return functions.get(name);
   }

   /**
    * Adds a new function to the map.
    * @param name the name of the function
    * @param function the function
    */
   public Function put(final String name, final FunctionTarget function) {
      CompositeFunction composite = functions.get(name);
      if(composite == null) {
         composite = new CompositeFunction(name);
         functions.put(name, composite);
      }
      composite.addTarget(function);
      return composite;
   }
}
