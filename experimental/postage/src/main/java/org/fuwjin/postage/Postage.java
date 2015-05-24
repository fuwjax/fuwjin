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
package org.fuwjin.postage;

import java.util.Arrays;
import java.util.List;

/**
 * The interface to shiny happy reflection. Postage exposes Java elements as
 * Functions, standardized execution hooks.
 */
public class Postage implements FunctionFactory {
   /**
    * Returns true if result is not a failure.
    * @param result the possible failure
    * @return false if result is a failure, true otherwise
    */
   public static boolean isSuccess(final Object result) {
      return Failure.isSuccess(result);
   }

   private final List<FunctionFactory> factories;

   /**
    * Creates a new instance.
    * @param factories custom factories for extending the standard reflection
    *        hooks
    */
   public Postage(final FunctionFactory... factories) {
      this.factories = Arrays.asList(factories);
   }

   /**
    * Returns a function instance for the name.
    * @param name the function name
    * @return the function
    * @throws IllegalArgumentException if the name does not map to a function
    */
   @Override
   public Function getFunction(final String name) {
      for(final FunctionFactory factory: factories) {
         final Function function = factory.getFunction(name);
         if(function != null) {
            return function;
         }
      }
      throw new IllegalArgumentException("No function found for " + name);
   }
}
