package org.fuwjin.grin.env;

import org.fuwjin.dinah.adapter.StandardAdapter;

public abstract class AbstractScope implements Scope {
   private final AbstractScope parent;

   public AbstractScope(final AbstractScope parent) {
      this.parent = parent;
   }

   @Override
   public Object get(final String name) {
      AbstractScope env = this;
      while(!env.containsImpl(name)) {
         env = env.parent;
      }
      final Object value = env.getImpl(name);
      if(this != env && StandardAdapter.isSet(value)) {
         put(name, value);
      }
      return value;
   }

   protected abstract boolean containsImpl(String name);

   protected abstract Object getImpl(String name);

   protected abstract AbstractScope newScope();

   protected AbstractScope parent() {
      return parent;
   }
}
