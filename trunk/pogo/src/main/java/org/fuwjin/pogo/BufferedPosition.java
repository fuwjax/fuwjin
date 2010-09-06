package org.fuwjin.pogo;

import org.fuwjin.postage.Adaptable;

/**
 * A Position that allows replay and buffered access to the underlying
 * CodePointStream.
 */
public interface BufferedPosition extends Position {
   /**
    * Flushes the buffer through to the last position.
    * @param last the last position
    * @return the new unbuffered last position
    */
   Position flush(Position last);

   /**
    * Returns the matched characters from the stream.
    * @param next the end position to buffer
    * @return the buffer as an adaptable
    */
   Adaptable match(Position next);
}
