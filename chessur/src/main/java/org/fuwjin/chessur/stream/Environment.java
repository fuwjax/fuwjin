package org.fuwjin.chessur.stream;

import java.util.HashMap;
import java.util.Map;
import org.fuwjin.util.Adapter;

public class Environment {
   public static final Environment NONE = new Environment() {
      @Override
      protected boolean contains(final String name) {
         return true;
      }

      @Override
      protected Object get(final String name) {
         return Adapter.unset();
      }
   };
   private final Map<String, Object> map = new HashMap<String, Object>();
   private final Environment parent;
   private int version = 0;

   public Environment(final Map<String, ? extends Object> map) {
      this(NONE);
      this.map.putAll(map);
   }

   Environment() {
      this((Environment)null);
   }

   private Environment(final Environment parent) {
      this.parent = parent;
   }

   public void assign(final String name, final Object value) {
      map.put(name, value);
      version++;
   }

   public Environment newScope() {
      return new Environment(this);
   }

   public Object retrieve(final String name) {
      Environment env = this;
      while(!env.contains(name)) {
         env = env.parent;
      }
      final Object value = env.get(name);
      if(this != env && Adapter.isSet(value)) {
         map.put(name, value);
      }
      return value;
   }

   public int version() {
      return version;
   }

   protected boolean contains(final String name) {
      return map.containsKey(name);
   }

   protected Object get(final String name) {
      return map.get(name);
   }
}
