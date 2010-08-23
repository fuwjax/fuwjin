package org.fuwjin.io;

public class IntRangeSet {
   private int[] values = new int[10];
   private int size;

   public void clear() {
      size = 0;
   }

   private String of(final int index) {
      return new String(Character.toChars(values[index]));
   }

   private String toString(final int index) {
      if(values[index] == values[index + 1]) {
         return of(index);
      }
      return of(index) + "-" + of(index + 1);
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
