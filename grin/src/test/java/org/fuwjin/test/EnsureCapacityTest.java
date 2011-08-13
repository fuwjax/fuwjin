package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.Random;
import org.fuwjin.grin.env.AbstractSource;
import org.junit.Test;

public class EnsureCapacityTest {
   private AbstractSource source = newSource();
   private final Random r = new Random();

   @Test
   public void testMarkCapacity() throws Exception {
      for(int i = 0; i < 10000; i++) {
         source = newSource();
         int offset = 0;
         for(int k = 0; k < 10; k++) {
            final int init = r.nextInt(64);
            for(int x = 0; x < init; x++) {
               read(offset++);
            }
            final int mark = source.mark();
            final int line = source.line();
            final int column = source.column();
            final int marked = r.nextInt(32);
            for(int j = 0; j < marked; j++) {
               read(offset + j);
            }
            source.seek(mark, line, column);
            source.release(mark);
         }
         source.mark();
         for(int x = 0; x < 31; x++) {
            read(offset++);
         }
         try {
            read(offset++);
            fail("should not grow buffer past max factor");
         } catch(final IllegalStateException e) {
            // must not exceed max length
         }
      }
   }

   @Test
   public void testSourceCapacity() throws Exception {
      for(int i = 0; i < 1000; i++) {
         read(i);
      }
   }

   protected AbstractSource newSource() {
      return new AbstractSource(null, 3, 5) {
         private int index;

         @Override
         protected char readChar() throws IOException {
            return (char)index++;
         }
      };
   }

   protected void read(final int i) throws Exception {
      assertThat(source.next(), is(i));
      source.read();
   }
}
