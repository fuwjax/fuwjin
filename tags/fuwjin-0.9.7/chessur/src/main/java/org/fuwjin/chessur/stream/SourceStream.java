package org.fuwjin.chessur.stream;

import org.fuwjin.chessur.expression.ResolveException;

/**
 * An abstraction stream for accepting.
 */
public interface SourceStream {
   /**
    * Attaches the stream to this stream.
    * @param stream the stream
    */
   void attach(SourceStream stream);

   SourceStream buffer();

   /**
    * Returns the current position.
    * @return the current position
    */
   Position current();

   /**
    * Forks this input stream.
    * @return the forked stream
    */
   SourceStream detach();

   int index();

   /**
    * Returns the next position in the stream.
    * @param snapshot the snapshot for error reporting
    * @return the next position
    * @throws ResolveException if it fails
    */
   Position next(Snapshot snapshot) throws ResolveException;

   /**
    * Returns the next position in the stream and advances the current pointer.
    * @param snapshot the snapshot for error reporting
    * @return the next position
    * @throws ResolveException if it fails
    */
   Position read(Snapshot snapshot) throws ResolveException;

   Position readAt(Snapshot snapshot, int index) throws ResolveException;
}
