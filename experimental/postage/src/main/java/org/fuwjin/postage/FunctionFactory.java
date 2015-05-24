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

/**
 * A factory for Function instances.
 */
public interface FunctionFactory {
   /**
    * Returns a function instance for the name. Returns null if no Function
    * could be found for the name.
    * @param name the function name.
    * @return the function, or null if no function exists
    */
   Function getFunction(String name);
}
