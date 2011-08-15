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

import java.lang.reflect.InvocationTargetException;
import org.fuwjin.dinah.Adapter.AdaptException;

/**
 * The reflection abstraction interface. All methods, constructors, virtual
 * methods, field mutation/access, keywords and other such invocation targets
 * are exposed through this interface outside of Dinah.
 */
public interface Function {
   /**
    * Invokes this function with the supplied arguments.
    * @param args the function arguments
    * @return the function result
    * @throws AdaptException if the arguments could not be adapted to the
    *         underlying invocation target
    * @throws InvocationTargetException if the underlying invocation target
    *         threw an exception
    */
   Object invoke(Object... args) throws AdaptException, InvocationTargetException;

   FunctionSignature signature();
}
