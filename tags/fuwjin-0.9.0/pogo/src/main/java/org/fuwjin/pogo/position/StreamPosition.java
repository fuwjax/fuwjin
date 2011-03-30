package org.fuwjin.pogo.position;

import java.io.IOException;

import org.fuwjin.pogo.BufferedPosition;
import org.fuwjin.pogo.CodePointStream;

public class StreamPosition extends AbstractInternalPosition {
   private final CodePointStream stream;
   private final int ch;
   private boolean hasNext;

   public StreamPosition(final CodePointStream stream) {
      this.stream = stream;
      ch = stream.next();
      accept(ch);
      // System.out.println(this);
   }

   protected StreamPosition(final StreamPosition prev) {
      super(prev);
      stream = prev.stream;
      ch = stream.next();
      accept(ch);
      // System.out.println(this);
   }

   @Override
   public void append(final Appendable builder) {
      try {
         if(ch == -1) {
            builder.append("EOF");
         } else {
            builder.append(new String(Character.toChars(ch)));
         }
      } catch(final IOException e) {
         fail("failed append", e);
      }
   }

   @Override
   public BufferedPosition buffered() {
      return new InternalChainedPosition(this);
   }

   @Override
   public void check(final int low, final int high) {
      if(ch < low || ch > high) {
         fail(low, high);
      } else {
         success();
      }
   }

   @Override
   public StreamPosition next() {
      assert !hasNext: this;
      hasNext = true;
      return new StreamPosition(this);
   }
}