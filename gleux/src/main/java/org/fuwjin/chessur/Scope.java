package org.fuwjin.chessur;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.chessur.InStream.Position;
import org.fuwjin.util.Adapter;

/**
 * Defines a variable scope.
 */
public class Scope {
   private final Scope parent;
   private Position mark;
   private final Map<String, Object> map = new HashMap<String, Object>();

   /**
    * Creates a new instance.
    */
   public Scope() {
      parent = null;
   }

   /**
    * Creates a new instance.
    * @param initialScope the initial scope
    */
   public Scope(final Map<String, ? extends Object> initialScope) {
      parent = null;
      map.putAll(initialScope);
   }

   private Scope(final Scope parent, final Position input) {
      this.parent = parent;
      mark = input;
   }

   private Scope(final Scope parent, final String name, final Object value) {
      this.parent = parent;
      map.put(name, value);
   }

   /**
    * Assigns a new name-value pair to the scope.
    * @param name the new name
    * @param value the new value
    * @return the new scope
    */
   public Scope assign(final String name, final Object value) {
      return new Scope(this, name, value);
   }

   /**
    * Returns the last marked position.
    * @return the last marked position
    */
   public InStream.Position mark() {
      if(mark == null) {
         return parent.mark();
      }
      return mark;
   }

   /**
    * Marks an input position.
    * @param input the input position
    * @return the new scope
    */
   public Scope mark(final Position input) {
      return new Scope(this, input);
   }

   /**
    * Retrieves the current value of a variable.
    * @param name the name of a variable.
    * @return the current value, or UNSET if there is no value
    */
   public Object retrieve(final String name) {
      Object value = map.get(name);
      if(value == null && !map.containsKey(name)) {
         if(parent != null) {
            value = parent.retrieve(name);
         } else {
            value = Adapter.unset();
         }
         map.put(name, value);
      }
      return value;
   }
}
