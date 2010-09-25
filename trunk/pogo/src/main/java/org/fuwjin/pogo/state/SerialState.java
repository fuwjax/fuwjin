package org.fuwjin.pogo.state;

import java.io.IOException;

/**
 * Manages the state of a serial operation.
 */
public class SerialState extends AbstractState {
   private final StringBuilder builder = new StringBuilder();
   private final Appendable appender;

   /**
    * Creates a new instance.
    * @param appender the destination of the serilaization
    */
   public SerialState(final Appendable appender) {
      this.appender = appender;
      super.set(new SerialPosition(this));
   }

   @Override
   public boolean advance(final int start, final int end) {
      String out;
      if(start == end) {
         out = new String(Character.toChars(start));
      } else if(start == 0 && end == Integer.MAX_VALUE) {
         out = String.valueOf(getValue());
      } else {
         fail("Character class not supported by serialization", null);
         return false;
      }
      append(out);
      int newLine = current().line();
      int newCol = current().column() + out.length();
      int index = out.indexOf('\n');
      while(index > -1) {
         newLine++;
         newCol = out.length() - index - 1;
         index = out.indexOf('\n', index + 1);
      }
      super.set(new SerialPosition(current(), builder.length(), newLine, newCol));
      return true;
   }

   private void append(final String out) {
      if(shouldBufferNext()) {
         builder.append(out);
      } else {
         appendImpl(out);
      }
   }

   private void appendImpl(final CharSequence seq) {
      try {
         appender.append(seq);
      } catch(final IOException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public PogoPosition buffer(final boolean required) {
      return PogoPosition.NULL;
   }

   @Override
   public SerialPosition current() {
      return (SerialPosition)super.current();
   }

   @Override
   protected void release() {
      super.release();
      if(!shouldBufferNext()) {
         appendImpl(builder);
         builder.setLength(0);
         current().setStart(0);
      }
   }

   @Override
   protected void set(final AbstractPosition pos) {
      super.set(pos);
      builder.setLength(current().start());
   }
}
