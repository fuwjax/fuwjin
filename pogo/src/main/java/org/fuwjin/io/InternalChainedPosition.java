package org.fuwjin.io;

import java.util.concurrent.atomic.AtomicInteger;

public class InternalChainedPosition extends PositionDecorator {
   private final AtomicInteger buffering;
   private InternalChainedPosition next;

   public InternalChainedPosition(final InternalPosition position) {
      this(position, new AtomicInteger(1));
   }

   private InternalChainedPosition(final InternalPosition position, final AtomicInteger buffering) {
      super(position);
      this.buffering = buffering;
   }

   @Override
   public Position advance(final int low, final int high) {
      check(low, high);
      if(isSuccess()) {
         return next();
      }
      return this;
   }

   @Override
   public BufferedPosition buffered() {
      buffering.incrementAndGet();
      return this;
   }

   @Override
   public Position flush(final Position last) {
      assert isBuffered();
      if(buffering.decrementAndGet() == 0) {
         return ((InternalPosition)last).root();
      }
      return last;
   }

   protected boolean isBuffered() {
      return buffering.get() > 0;
   }

   @Override
   public Object match(final Position last) {
      return new Object() {
         private String str;

         @Override
         public String toString() {
            if(str == null) {
               str = substring(last);
            }
            return str;
         }
      };
   }

   @Override
   public InternalPosition next() {
      if(next == null) {
         if(!isBuffered()) {
            return super.next();
         }
         next = new InternalChainedPosition(super.next(), buffering);
      }
      return next;
   }

   private String substring(final Position last) {
      final StringBuilder builder = new StringBuilder();
      InternalChainedPosition p = InternalChainedPosition.this;
      while(p != last) {
         assert p != null;
         p.append(builder);
         p = p.next;
      }
      return builder.toString();
   }

   @Override
   public BufferedPosition unbuffered() {
      return buffered();
   }
}
