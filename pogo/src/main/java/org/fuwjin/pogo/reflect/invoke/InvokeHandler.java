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
package org.fuwjin.pogo.reflect.invoke;

/**
 * Handles message dispatch to a target object.
 */
public interface InvokeHandler {
   /**
    * Indicates a failure to attempt an invoke.
    */
   Object FAILURE = new Object();

   /**
    * Triggers the dispatch to the underlying invocation operation.
    * @param target the target of the message dispatch
    * @param args the arguments for the dispatch
    * @return the result of the invocation or FAILURE if the dispatch could not
    *         be attempted
    * @throws Exception if the invocation is failure
    */
   Object invoke(Object target, Object... args) throws Exception;

   /**
    * Returns the parameter types for a given count.
    * @param paramCount the number of types to return
    * @return the array of types of length {@code paramCount}, or null if that
    *         number of types is not supported
    */
   Class<?>[] paramTypes(int paramCount);
}
