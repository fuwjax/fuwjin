package org.fuwjin.jon;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.Function;
import org.fuwjin.util.TypeUtils;

public class Container extends Reference {
   private final Object vessel;
   private int pending;
   private Type type;
   private Adapter adapter;

   public Container(final Object vessel) {
      this.vessel = vessel;
   }

   public void adapt(final Adapter adapter, final Type type) {
      this.adapter = adapter;
      this.type = type;
   }

   public void add(final Object value) throws Exception {
      if(value instanceof Reference) {
         ++pending;
         final Reference ref = (Reference)value;
         if(vessel instanceof List) {
            final List list = (List)vessel;
            final int index = list.size();
            list.add(null);
            ref.addTask(new ListTask(this, index));
         } else {
            ref.addTask(new CollectionTask(this));
         }
      } else {
         ((Collection)vessel).add(value);
         checkResolve();
      }
   }

   public void put(final Object key, final Object value) throws Exception {
      if(key instanceof Reference) {
         ++pending;
         ((Reference)key).addTask(new MapKeyTask(this, value));
      } else if(value instanceof Reference) {
         ++pending;
         ((Reference)value).addTask(new MapValueTask(this, key));
      } else {
         ((Map)vessel).put(key, value);
         checkResolve();
      }
   }

   public void set(final Function setter, final Object value) throws Exception {
      if(value instanceof Reference) {
         ++pending;
         ((Reference)value).addTask(new SetterTask(this, setter));
      } else {
         setter.invoke(vessel, value);
         checkResolve();
      }
   }

   public void set(final int index, final Object value) throws Exception {
      if(value instanceof Reference) {
         ++pending;
         ((Reference)value).addTask(new ListTask(this, index));
      } else {
         ((List)vessel).set(index, value);
         checkResolve();
      }
   }

   private void checkResolve() throws Exception {
      if(--pending == 0) {
         if(type == null || TypeUtils.isInstance(type, vessel)) {
            resolve(vessel);
         } else {
            resolve(adapter.adapt(vessel, type));
         }
      }
   }
}
