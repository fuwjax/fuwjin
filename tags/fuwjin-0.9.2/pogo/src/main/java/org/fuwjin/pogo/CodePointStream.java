package org.fuwjin.pogo;

/**
 * Allows code points or bytes to be streamed one at a time.
 */
public interface CodePointStream {
   /**
    * The End of File marker. Once a stream returns EOF, it will only return
    * EOF.
    */
   int EOF = -1;

   /**
    * Returns the next code point or byte, or EOF if the end of the stream has
    * been reached.
    * @return the next code point.
    */
   int next();
}
