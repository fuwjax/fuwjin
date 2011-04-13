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
package org.fuwjin.chessur;

import java.util.LinkedHashMap;
import java.util.Map;

public class NameIndex {
   private final Map<String, Integer> indicies = new LinkedHashMap<String, Integer>();

   public Iterable<Map.Entry<String, Integer>> entries() {
      return indicies.entrySet();
   }

   public int indexOf(final String name) {
      Integer index = indicies.get(name);
      if(index == null) {
         index = indicies.size();
         indicies.put(name, index);
      }
      return index;
   }

   public int size() {
      return indicies.size();
   }
}
