package org.fuwjin.pogo.state;

import org.fuwjin.pogo.PogoException;
import org.fuwjin.postage.Failure;

/**
 * An interface for interacting with ParseExpressions.
 */
public interface PogoState {
   /**
    * Advances the current position, if the filter matches the input.
    * @param start the lowest allowed code point
    * @param end the highest allowed code point
    * @return true if the position advanced, false otherwise
    */
   boolean advance(final int start, final int end);

   /**
    * Returns a buffered position. The toString of this position is the matched
    * input if the required parameter is true. The toString may return null if
    * required is false. The buffer must be released.
    * @param required true if the buffer's toString must be set, false otherwise
    * @return the buffered position
    */
   PogoPosition buffer(final boolean required);

   /**
    * Returns the current position. This position should not be released.
    * @return the current position
    */
   PogoPosition current();

   /**
    * Returns an exception for this state. This method will always generate an
    * exception, but it is only valid if the parse returns false.
    * @return the exception
    */
   PogoException exception();

   /**
    * Fails the parse at the current position.
    * @param string the failure message
    * @param cause the failure cause
    */
   void fail(final String string, final Failure cause);

   /**
    * Returns a memo for the given rule name. If the memo already exists, the
    * current position is updated to the end position of the memo.
    * @param name the rule name
    * @param needsBuffer true if the memo must have a valid buffer, false
    *        otherwise
    * @return the memo
    */
   PogoMemo getMemo(final String name, final boolean needsBuffer);

   /**
    * Returns the currently associated object.
    * @return the value object
    */
   Object getValue();

   /**
    * Returns true if the state has moved past the marked position.
    * @param mark the marked position
    * @return true if the state is after the mark, false otherwise
    */
   boolean isAfter(final PogoPosition mark);

   /**
    * Marks the current position. This position must be released.
    * @return the current position.
    */
   PogoPosition mark();

   /**
    * Sets the currently associated object.
    * @param object the value object
    */
   void setValue(final Object object);
}