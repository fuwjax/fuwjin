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
package org.fuwjin.test.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A subclass of integer object with a list of strings
 */
public class IntegerChild extends IntegerObject {
   private final List<String> list;

   /**
    * Creates a new instance
    * @param strings an array of strings used to populate the list
    * @param id the parent's i field
    */
   public IntegerChild(final String[] strings, final int id) {
      super(id);
      list = new ArrayList<String>(Arrays.asList(strings));
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final IntegerChild o = (IntegerChild)obj;
         return list.equals(o.list) && super.equals(obj);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() + list.hashCode();
   }
}
