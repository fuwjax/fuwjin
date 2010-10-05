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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * The interface for managing Function invocation.
 */
public interface FunctionTarget {
   /**
    * Invokes this target with the supplied arguments.
    * @param args the invocation arguments
    * @return the invocation result
    * @throws InvocationTargetException if the invocation failed
    * @throws Exception if the arguments do not map to this target
    */
   Object invoke(Object[] args) throws InvocationTargetException, Exception;

   /**
    * Returns the index-th parameter type.
    * @param index the index of the parameter
    * @return the parameter type
    */
   Type parameterType(int index);

   /**
    * Returns the number of required arguments.
    * @return the number of required arguments
    */
   int requiredArguments();

   /**
    * Returns the return type.
    * @return the return type
    */
   Type returnType();
}
