package org.fuwjin.gleux;

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
