package org.fuwjin.util;

import java.util.Arrays;

public class ObjectUtils {
   public interface ObjectHelper {
      Object[] identity();
   }

   public static long hash(final ObjectHelper obj) {
      return Arrays.hashCode(obj.identity());
   }

   public static boolean isEqual(final ObjectHelper obj, final Object other) {
      if(obj == null && other == null) {
         return true;
      }
      if(obj == null || other == null) {
         return false;
      }
      if(obj == other) {
         return true;
      }
      if(obj.getClass().equals(other.getClass())) {
         return Arrays.equals(obj.identity(), ((ObjectHelper)other).identity());
      }
      return false;
   }

   public static String toString(final ObjectHelper obj) {
      return obj.getClass().getCanonicalName() + Arrays.toString(obj.identity());
   }
}
