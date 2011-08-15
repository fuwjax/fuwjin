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

   public static Object concatObject(final Object... args) {
      return new Object() {
         @Override
         public String toString() {
            final StringBuilder builder = new StringBuilder();
            for(final Object o: args) {
               builder.append(o);
            }
            return builder.toString();
         }
      };
   }

   public static String format(final String pattern, final Object... formatArgs) {
      return String.format(pattern, formatArgs);
   }

   public static Object formatObject(final String pattern, final Object... formatArgs) {
      return new Object() {
         @Override
         public String toString() {
            return format(pattern, formatArgs);
         }
      };
   }

   public static Object joinObject(final String join, final Iterable<?> list) {
      return new Object() {
         @Override
         public String toString() {
            final StringBuilder builder = new StringBuilder();
            boolean first = true;
            for(final Object o: list) {
               if(!first) {
                  builder.append(join);
               }
               first = false;
               builder.append(o);
            }
            return builder.toString();
         }
      };
   }

   private Object richMessage;

   public BusinessException() {
      super();
   }

   /**
    * Creates a new instance.
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public BusinessException(final String pattern, final Object... args) {
      super(pattern);
      richMessage = formatObject(pattern, args);
   }

   public BusinessException(final Throwable cause) {
      super(cause);
   }

   /**
    * Creates a new instance.
    * @param cause the exception cause
    * @param pattern the message pattern
    * @param args the message arguments
    */
   public BusinessException(final Throwable cause, final String pattern, final Object... args) {
      super(pattern, cause);
      richMessage = formatObject(pattern, args);
   }

   @Override
   public synchronized Throwable fillInStackTrace() {
      return this;
   }

   @Override
   public String getMessage() {
      if(richMessage != null) {
         return richMessage.toString();
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

   protected synchronized Throwable restoreStackTrace() {
      return super.fillInStackTrace();
   }

   protected void setRichMessage(final Object rich) {
      richMessage = rich;
   }
}
