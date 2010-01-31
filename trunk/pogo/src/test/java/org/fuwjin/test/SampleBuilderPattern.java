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
package org.fuwjin.test;

/**
 * This class is used to exercise the rules.
 */
public class SampleBuilderPattern {
   /**
    * The null object for SBP.
    */
   public static SampleBuilderPattern NULL = new SampleBuilderPattern("static NULL;"); //$NON-NLS-1$

   /**
    * Static factory.
    * @return the new instance
    */
   public static SampleBuilderPattern newInstance() {
      return new SampleBuilderPattern("static newInstance;"); //$NON-NLS-1$
   }

   /**
    * Value of converter.
    * @param value the value to convert
    * @return the converted value
    */
   public static SampleBuilderPattern valueOf(final String value) {
      return new SampleBuilderPattern("static valueOf:" + value + ';'); //$NON-NLS-1$
   }

   /**
    * The sample state.
    */
   public final StringBuilder builder = new StringBuilder();

   /**
    * Default constructor.
    */
   public SampleBuilderPattern() {
      builder.append("default constructor;"); //$NON-NLS-1$
   }

   private SampleBuilderPattern(final String value) {
      builder.append(value);
   }

   /**
    * Append method.
    * @param child the child to append
    */
   public void addChild(final SampleBuilderPattern child) {
      builder.append("adding:" + child + ';'); //$NON-NLS-1$
   }

   /**
    * Creates a new child.
    * @return the new child
    */
   public SampleBuilderPattern newChild() {
      return new SampleBuilderPattern("new child;"); //$NON-NLS-1$
   }

   /**
    * Creates the result.
    * @return the result
    */
   public String toResult() {
      return builder.append("toResult;").toString(); //$NON-NLS-1$
   }

   @Override
   public String toString() {
      return builder.toString();
   }
}
