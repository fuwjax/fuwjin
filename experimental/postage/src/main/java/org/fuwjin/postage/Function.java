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

import java.lang.reflect.Type;

import org.fuwjin.postage.Failure.FailureException;

/**
 * The invocation handler API.
 */
public interface Function {
   /**
    * Preserves arguments for partial and deferred invocation.
    * @param args the arguments to defer to a later invocation
    * @return the function with the preserved arguments
    */
   Function curry(Object... args);

   /**
    * Transforms the inner target set according to the filter.
    * @param filter the filter used to transform the inner targets
    * @return the new function
    */
   Function filter(TargetTransform filter);

   /**
    * Invokes the function without Failure. Calls to this method will never
    * result in an instance of failure, and can therefore be used unchecked.
    * @param args the arguments to pass to the function
    * @return the result
    * @throws FailureException if the invocation fails
    */
   Object invoke(Object... args) throws FailureException;

   /**
    * Invokes the function without exception. Calls to this method will never
    * result in an exception, instead a Failure instance will be returned in the
    * event of any failure.
    * @param args the arguments to pass to the function
    * @return the result, or a Failure instance if the invocation fails
    */
   Object invokeSafe(Object... args);

   /**
    * Returns the name.
    * @return the name
    */
   String name();

   /**
    * Returns the index-th parameter type.
    * @param index the index of the parameter
    * @return the parameter type
    */
   Type parameterType(int index);

   FunctionRenderer renderer(boolean castArguments);

   /**
    * Returns the return type.
    * @return the return type
    */
   Type returnType();

   /**
    * Returns a function that requires the parameter types and guarantees the
    * return type.
    * @param returnType the return type of the required function
    * @param parameters the parameter types of the required function
    * @return the function that supports the signature
    */
   Function withSignature(Type returnType, Type... parameters);
}
