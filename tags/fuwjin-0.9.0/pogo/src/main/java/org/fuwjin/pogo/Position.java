package org.fuwjin.pogo;

import org.fuwjin.postage.Failure;

/**
 * The state for the current position during a parse.
 */
public interface Position {
   /**
    * Moves to the next position if the current position meets the criteria.
    * @param low the minimum character
    * @param high the maximum character
    * @return the next position
    */
   Position advance(int low, int high);

   /**
    * Asserts that this position was consumed successfully.
    * @throws PogoException if the position was not consumed successfully
    */
   void assertSuccess() throws PogoException;

   /**
    * Buffers this position.
    * @return the buffered position
    */
   BufferedPosition buffered();

   /**
    * Creates a new memo for the rule-value pair.
    * @param name the rule name
    * @param value the memoized value
    * @return the memo
    */
   Memo createMemo(String name, Object value);

   /**
    * Fails the parse at this position.
    * @param reason the failure reason
    * @param cause the failure cause
    */
   void fail(String reason, Failure cause);

   /**
    * Returns true if this position is logically after the given position.
    * @param position the test position
    * @return true if this position is after the given position, false otherwise
    */
   boolean isAfter(Position position);

   /**
    * Returns true if this position was consumed successfully.
    * @return true if consumed successfully, false otherwise
    */
   boolean isSuccess();

   /**
    * Returns the current memo.
    * @return the memo
    */
   Memo memo();

   /**
    * Releases the current memo and restores the given new memo.
    * @param newMemo the new memo
    * @return the old memo
    */
   Memo releaseMemo(Memo newMemo);

   /**
    * Marks this position as a success.
    */
   void success();

   /**
    * Returns this position as a mock buffered position.
    * @return the position unbuffered
    */
   BufferedPosition unbuffered();
}
