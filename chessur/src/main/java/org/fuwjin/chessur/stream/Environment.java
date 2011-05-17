package org.fuwjin.chessur.stream;

import java.util.HashMap;
import java.util.Map;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.adapter.StandardAdapter;

/**
 * The scoped variable mapping.
 */
public class Environment {
   /**
    * Constant for no initial environment.
    */
   public static final Environment NONE = new Environment() {
      @Override
      protected boolean contains(final String name) {
         return true;
      }

      @Override
      protected Object get(final String name) {
         return Adapter.UNSET;
      }
   };
   private final Map<String, Object> map = new HashMap<String, Object>();
   private final Environment parent;

   /**
    * Creates a new initial environment from the map.
    * @param map the initial environment mapping.
    */
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

   /**
    * Assigns the value to the variable name in this scope.
    * @param name the variable name
    * @param value the value
    */
   public void assign(final String name, final Object value) {
      map.put(name, value);
   }

   /**
    * Returns a new sub-environment.
    * @return the new environment
    */
   public Environment newScope() {
      return new Environment(this);
   }

   /**
    * Returns the value of the variable in this scope. Returns UNSET if not yet
    * defined.
    * @param name the variable name
    * @return the value of the variable, or UNSET if not yet set
    */
   public Object retrieve(final String name) {
      Environment env = this;
      while(!env.contains(name)) {
         env = env.parent;
      }
      final Object value = env.get(name);
      if(this != env && StandardAdapter.isSet(value)) {
         map.put(name, value);
      }
      return value;
   }

   protected boolean contains(final String name) {
      return map.containsKey(name);
   }

   protected Object get(final String name) {
      return map.get(name);
   }
}
