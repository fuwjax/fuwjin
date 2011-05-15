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
package org.fuwjin.dinah;

/**
 * A Factory method for Function instances. Function instances returned by this
 * method need not be unique, however, if a Function instance could be shared
 * across threads, it must be threadsafe and/or stateless.
 */
public interface FunctionProvider {
   /**
    * Returns the function corresponding to the signature.
    * @param signature the required function signature
    * @return the corresponding function
    * @throws IllegalArgumentException if the signature cannot be mapped to a
    *         function on this provider
    */
   Function getFunction(FunctionSignature signature);
}
