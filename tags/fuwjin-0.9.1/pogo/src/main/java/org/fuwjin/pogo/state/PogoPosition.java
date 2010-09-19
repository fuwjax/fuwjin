package org.fuwjin.pogo.state;

/**
 * Marks a position during a pogo operation.
 */
public interface PogoPosition {
   /**
    * The null position.
    */
   PogoPosition NULL = new PogoPosition() {
      @Override
      public void release() {
         // do nothing
      }

      @Override
      public void reset() {
         // do nothing
      }

      @Override
      public String toString() {
         return null;
      }
   };

   /**
    * Releases the position.
    */
   void release();

   /**
    * Resets the state to this position.
    */
   void reset();

   /**
    * Returns the line/column number normally, but in the case of a
    * {@link PogoState#buffer(boolean)}, returns the buffered string.
    * @return the line/column or the matched string.
    */
   @Override
   String toString();
}