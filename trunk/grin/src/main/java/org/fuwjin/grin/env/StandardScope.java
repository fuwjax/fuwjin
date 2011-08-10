package org.fuwjin.grin.env;

import java.util.HashMap;
import java.util.Map;

public class StandardScope extends AbstractScope {
   private final Map<String, Object> map = new HashMap<String, Object>();

   public StandardScope() {
      super(StandardEnv.NO_SCOPE);
   }

   public StandardScope(final Map<String, ? extends Object> environment) {
      super(StandardEnv.NO_SCOPE);
      map.putAll(environment);
   }

   protected StandardScope(final AbstractScope parent) {
      super(parent);
   }

   @Override
   public AbstractScope newScope() {
      return new StandardScope(this);
   }

   @Override
   public void put(final String name, final Object value) {
      map.put(name, value);
   }

   @Override
   protected boolean containsImpl(final String name) {
      return map.containsKey(name);
   }

   @Override
   protected Object getImpl(final String name) {
      return map.get(name);
   }
}
