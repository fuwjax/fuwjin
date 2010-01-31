package org.fuwjin.jon.container;

import java.util.HashMap;
import java.util.Map;

public class JonContainer {
   private final Map<String, Object> references = new HashMap<String, Object>();

   public Object get(final String name) {
      Object ref = references.get(name);
      if(ref == null) {
         ref = new ContainerProxy();
         references.put(name, ref);
      }
      return ref;
   }

   public void store(final String name, final Object value) {
      final Object forward = references.put(name, value);
      if(forward != null && !forward.equals(value)) {
         ((ContainerProxy)forward).resolve(value);
      }
   }
}
