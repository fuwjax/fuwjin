package org.fuwjin.pogo.attr;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Specifies a bridge instruction between Pogo and the underlying language, in
 * this case Java.
 */
public interface Attribute extends Expression {
   /**
    * Prepares the state for executing a result expression.
    * @param state the state to prepare
    * @param scope the scope to prepare
    */
   void prepare(State state, Map<String, Object> scope);

   /**
    * Returns the type of the returned value.
    * @return the attribute type
    */
   Type type();

   /**
    * Returns the value according to the state.
    * @param state TODO
    * @param scope the state for the value generation
    * @return the value
    */
   Object value(State state, Map<String, Object> scope);
}
