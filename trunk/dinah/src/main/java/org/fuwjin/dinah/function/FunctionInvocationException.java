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
package org.fuwjin.dinah.function;

import org.fuwjin.util.BusinessException;

public class FunctionInvocationException extends BusinessException{
   private static final long serialVersionUID = 1L;

   public FunctionInvocationException(final String pattern, final Object... args){
      super(pattern, args);
   }

   public FunctionInvocationException(final Throwable cause, final String pattern, final Object... args){
      super(cause, pattern, args);
   }
}
