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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides a sequential indexed mapping for names.
 */
public class NameIndex {
   private final Map<String, Integer> indicies = new LinkedHashMap<String, Integer>();

   /**
    * Returns the set of mappings.
    * @return the set of mappings
    */
   public Iterable<Map.Entry<String, Integer>> entries() {
      return indicies.entrySet();
   }

   /**
    * Returns the index for the name.
    * @param name the name to index
    * @return the index
    */
   public int indexOf(final String name) {
      Integer index = indicies.get(name);
      if(index == null) {
         index = indicies.size();
         indicies.put(name, index);
      }
      return index;
   }

   /**
    * Returns the number of mappings.
    * @return the number of mappings
    */
   public int size() {
      return indicies.size();
   }
}
