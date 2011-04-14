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
package org.fuwjin.util;

/**
 * Creates an exception that defers as much of the "bad stuff" associated with
 * exceptions as possible. In particular, there are no stack traces available,
 * and the message may be deferred until it is requested.
 */
public class BusinessException extends Exception {
   private static final long serialVersionUID = 1L;
   private final Object[] args;

   /**
    * Creates a new instance.
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public BusinessException(final String pattern, final Object... args) {
      super(pattern);
      this.args = args;
   }

   /**
    * Creates a new instance.
    * @param cause the exception cause
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public BusinessException(final Throwable cause, final String pattern, final Object... args) {
      super(pattern, cause);
      this.args = args;
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }

   @Override
   public String getMessage() {
      if(args != null && args.length > 0) {
         return format(args);
      }
      return super.getMessage();
   }

   @Override
   public StackTraceElement[] getStackTrace() {
      if(getCause() != null && getCause() != this) {
         return getCause().getStackTrace();
      }
      return new StackTraceElement[0];
   }

   protected String format(final Object[] formatArgs) {
      return String.format(super.getMessage(), formatArgs);
   }
}
