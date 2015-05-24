package org.fuwjin.pogo.attr;

/**
 * An input buffer for the Match attribute.
 */
public interface Buffer {
   /**
    * Releases the buffer and returns the current match result.
    * @param state the state to buffer to
    * @return the match between the state at construction and this state
    */
   String release(State state);
}
