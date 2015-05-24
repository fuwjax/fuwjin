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
package org.fuwjin.sample;

import java.util.Observable;

public class SampleObject implements TrivialInterface {
   static String staticField;

   public static String sampleStatic(final String arg) {
      return arg;
   }

   String field;
   public Object updated;

   public SampleObject() {
      // ignore
   }

   public SampleObject(final String arg) {
      // ignore
   }

   public void fails() {
      throw new NullPointerException();
   }

   public String sample() {
      return "test";
   }

   @Override
   public void update(final Observable o, final Object arg) {
      updated = arg;
   }
}
