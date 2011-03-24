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
package org.fuwjin.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A partition set for code points. Partition sets are an attempt at managing a
 * set of values by tracking ranges instead of individual elements. This set
 * tracks int ranges under the assumption that they represent code points.
 */
public class CodePointSet {
   /**
    * Represents a range or a single character.
    */
   public static class Range {
      final int start;
      final int end;
      final boolean isRange;

      /**
       * Creates a new instance.
       * @param start the start character
       * @param end the end character
       */
      public Range(final int start, final int end) {
         this.start = start;
         this.end = end;
         isRange = start != end;
      }

      public Iterable<Integer> chars() {
         return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
               return new Iterator<Integer>() {
                  private int index = start;

                  @Override
                  public boolean hasNext() {
                     return index <= end;
                  }

                  @Override
                  public Integer next() {
                     if(!hasNext()) {
                        throw new NoSuchElementException();
                     }
                     return index++;
                  }

                  @Override
                  public void remove() {
                     throw new UnsupportedOperationException();
                  }
               };
            }
         };
      }

      @Override
      public String toString() {
         return new String(Character.toChars(start)) + "-" + new String(Character.toChars(end));
      }
   }

   private int[] values = new int[10];
   private int size;

   /**
    * Clears the set.
    */
   public void clear() {
      size = 0;
   }

   /**
    * Returns true if this set contains the code point.
    * @param codePoint the code point
    * @return true if the set contains the code point, false otherwise
    */
   public boolean contains(final int codePoint) {
      for(int index = 0; index < size; index += 2) {
         if(values[index] <= codePoint && values[index + 1] >= codePoint) {
            return true;
         }
      }
      return false;
   }

   private String of(final int index) {
      return new String(Character.toChars(values[index]));
   }

   /**
    * Returns the set of ranges.
    * @return the ranges
    */
   public Iterable<Range> ranges() {
      return new Iterable<Range>() {
         @Override
         public Iterator<Range> iterator() {
            return new Iterator<Range>() {
               private int index = 0;

               @Override
               public boolean hasNext() {
                  return index < size;
               }

               @Override
               public Range next() {
                  if(!hasNext()) {
                     throw new NoSuchElementException();
                  }
                  return new Range(values[index++], values[index++]);
               }

               @Override
               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   @Override
   public String toString() {
      if(values[0] == 0 && values[1] == Integer.MAX_VALUE) {
         return "...";
      }
      final StringBuilder builder = new StringBuilder();
      for(int i = 0; i < size; i += 2) {
         builder.append(toString(i));
      }
      return builder.toString();
   }

   private String toString(final int index) {
      if(values[index] == values[index + 1]) {
         return of(index);
      }
      return of(index) + "-" + of(index + 1);
   }

   /**
    * Performs an in-place union of this set with the set [low-high]. Both low
    * and high are included in the union.
    * @param low the low code point in the range
    * @param high the high code point in the range
    */
   public void unionRange(final int low, final int high) {
      int trim = Integer.MAX_VALUE;
      for(int index = 0; index < size; index += 2) {
         if(values[index] >= low) {
            if(values[index] > high + 1) {
               final int[] temp = values;
               if(size == values.length) {
                  values = new int[size * 2];
                  System.arraycopy(temp, 0, values, 0, index);
               }
               System.arraycopy(temp, index, values, index + 2, size - index);
               values[index] = low;
               values[index + 1] = high;
               size += 2;
               return;
            } else if(index < trim) {
               values[index] = low;
               trim = index;
            }
         }
         if(values[index + 1] >= low - 1) {
            if(values[index + 1] > high) {
               if(trim < index) {
                  System.arraycopy(values, index + 1, values, index + 1 - index + trim, size - index - 1);
                  size = size - index + trim;
               }
               return;
            } else if(index < trim) {
               trim = index;
            }
         }
      }
      if(trim < size) {
         size = trim + 1;
      } else {
         if(size == values.length) {
            final int[] temp = values;
            values = new int[size * 2];
            System.arraycopy(temp, 0, values, 0, size);
         }
         values[size++] = low;
      }
      values[size++] = high;
   }
}
