package org.fuwjin.chessur;

/**
 * Transforms a state.
 */
public interface Expression {
   /**
    * Transforms a state.
    * @param state the input state
    * @return the output state
    */
   State transform(State state);
}
