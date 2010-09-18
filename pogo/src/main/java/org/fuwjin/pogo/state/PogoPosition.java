package org.fuwjin.pogo.state;

public interface PogoPosition {
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

   void release();

   void reset();

   @Override
   String toString();
}