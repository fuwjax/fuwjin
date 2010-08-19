package org.fuwjin.io;

import java.io.IOException;
import java.util.Stack;

public class SerialStreamPosition extends AbstractInternalPosition {
   private String obj;
   private int ch = -1;
   private final Stack<Appendable> appenders;

   private SerialStreamPosition(final AbstractInternalPosition prev, final Stack<Appendable> appenders) {
      super(prev);
      this.appenders = appenders;
   }

   public SerialStreamPosition(final Appendable appender) {
      appenders = new Stack<Appendable>();
      appenders.push(appender);
   }

   @Override
   public InternalPosition advance(final int low, final int high) {
      check(low, high);
      if(!isSuccess()) {
         return this;
      }
      append(appenders.peek());
      return next();
   }

   @Override
   public void append(final Appendable appender) {
      try {
         if(ch == -1) {
            appender.append(obj);
         } else {
            appender.append(new String(Character.toChars(ch)));
         }
      } catch(final IOException e) {
         fail("failed append", e);
      }
   }

   @Override
   public BufferedPosition buffered() {
      appenders.push(new StringBuilder());
      return this;
   }

   @Override
   public void check(final int low, final int high) {
      if(low == high) {
         ch = low;
      }
      success();
   }

   @Override
   public Position flush(final Position last) {
      final Appendable flushed = appenders.pop();
      if(this != last) {
         final Appendable dest = appenders.peek();
         if(flushed != dest) {
            try {
               dest.append(flushed.toString());
            } catch(final IOException e) {
               fail("failed flush", e);
            }
         }
      }
      return last;
   }

   @Override
   public InternalPosition next() {
      final SerialStreamPosition next = new SerialStreamPosition(this, appenders);
      if(ch == -1) {
         next.accept(obj);
      } else {
         next.accept(ch);
      }
      return next;
   }

   @Override
   public void store(final String name, final Object state) {
      super.store(name, state);
      obj = String.valueOf(state);
   }

   @Override
   public BufferedPosition unbuffered() {
      appenders.push(appenders.peek());
      return this;
   }
}
