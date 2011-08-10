package org.fuwjin.grin.env;

/**
 * Grin environment.
 */
public interface Scope {
   Object get(String variable);

   /**
    * Sets a variable to a new value in the current scope.
    * @param variable the name of the variable to change
    * @param value the new value
    */
   void put(String variable, Object value);
}
