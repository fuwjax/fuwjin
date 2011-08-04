package org.fuwjin.chessur.stream;

import org.fuwjin.chessur.expression.AbortedException;

/**
 * An abstraction stream for publishing.
 */
public interface SinkStream {
   /**
    * Publishes the value to this stream.
    * @param value the value
    * @throws AbortedException if this stream has reached an unrecoverable state
    */
   void append(Object value) throws AbortedException;

   /**
    * Reattaches the stream to this stream.
    * @param stream the detached stream
    * @throws AbortedException if the stream has reached an unrecoverable state
    */
   void attach(SinkStream stream) throws AbortedException;

   /**
    * Returns the current position.
    * @return the current position
    */
   Position current();

   /**
    * Forks the output for this script.
    * @return the detached stream
    */
   SinkStream detach();

   void log(Object value);
}
