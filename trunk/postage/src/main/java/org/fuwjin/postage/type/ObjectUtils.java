package org.fuwjin.postage.type;

import java.lang.reflect.AccessibleObject;

public class ObjectUtils {
   public static <T extends AccessibleObject> T access(final T obj) {
      if(!obj.isAccessible()) {
         obj.setAccessible(true);
      }
      return obj;
   }
}
