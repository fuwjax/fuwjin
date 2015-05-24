package org.fuwjin.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CodePointList implements Iterable<Integer> {
   private final List<Integer> ints = new ArrayList<Integer>();

   public void add(final int codePoint) {
      ints.add(codePoint);
   }

   @Override
   public Iterator<Integer> iterator() {
      return ints.iterator();
   }
}
