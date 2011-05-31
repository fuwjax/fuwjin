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

import org.fuwjin.util.BusinessException;

/**
 * A Factory method for Function instances. Function instances returned by this
 * method need not be unique, however, if a Function instance could be shared
 * across threads, it must be threadsafe and/or stateless.
 */
public interface FunctionProvider extends Adapter {
   /**
    * Thrown when no function can be found.
    */
   public class NoSuchFunctionException extends BusinessException {
      private static final long serialVersionUID = 1L;

      /**
       * Creates a new instance.
       * @param pattern the message pattern
       * @param args the message arguments
       */
      public NoSuchFunctionException(final String pattern, final Object... args) {
         super(pattern, args);
      }

      /**
       * Creates a new instance.
       * @param cause the cause
       * @param pattern the message pattern
       * @param args the message arguments
       */
      public NoSuchFunctionException(final Throwable cause, final String pattern, final Object... args) {
         super(pattern, cause, args);
      }
   }

   /**
    * Returns the function corresponding to the signature.
    * @param signature the required function signature
    * @return the corresponding function
    * @throws NoSuchFunctionException if the signature cannot be mapped to a
    *         function on this provider
    */
   Function getFunction(FunctionSignature signature) throws NoSuchFunctionException;
}
