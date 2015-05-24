package org.fuwjin.pogo.attr;

import java.util.Map;

/**
 * Specifies a process for transforming the State.
 */
public interface Expression {
   /**
    * Transitions the state according to the rules of this expression. Note that
    * while every State instance is immutable, the referenced objects may not
    * be. In this case, a state transition may have side effects.
    * @param state the state
    * @param scope TODO
    * @return the result of the transform
    */
   State transition(State state, Map<String, Object> scope);
}
