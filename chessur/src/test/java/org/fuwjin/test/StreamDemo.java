package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.fuwjin.chessur.expression.ResolveException;
import org.fuwjin.chessur.stream.CodePointInStream;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.ObjectOutStream;
import org.fuwjin.chessur.stream.Position;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.junit.Test;

/**
 * Unit test for stream behavior.
 */
public class StreamDemo {
   /**
    * Tests the buffered input.
    * @throws Exception if it fails
    */
   @Test
   public void testAttach() throws Exception {
      final SourceStream stream = CodePointInStream.streamOf("test\nx").buffer();
      final Snapshot snapshot = new Snapshot(stream, ObjectOutStream.NONE, Environment.NONE);
      stream.read(snapshot);
      stream.read(snapshot);
      stream.read(snapshot);
      final SourceStream detached = stream.detach();
      final Position p3 = detached.read(snapshot);
      assertThat((Integer)p3.value(), is((int)'t'));
      assertThat(stream.toString(), is("test"));
      assertThat(p3.toString(), is("[1,4] 't'"));
      final Position p4 = detached.read(snapshot);
      assertThat((Integer)p4.value(), is((int)'\n'));
      assertThat(stream.toString(), is("test\n"));
      assertThat(p4.toString(), is("[1,5] '\\n'"));
      stream.attach(detached);
      assertThat(stream.toString(), is("test\n"));
      final Position p5 = stream.read(snapshot);
      assertThat((Integer)p5.value(), is((int)'x'));
      assertThat(stream.toString(), is("test\nx"));
      assertThat(p5.toString(), is("[2,1] 'x'"));
      try {
         stream.read(snapshot);
         fail("should throw on EOF");
      } catch(final ResolveException e) {
         // pass
      }
   }

   /**
    * Tests the output stream.
    * @throws Exception if it fails
    */
   @Test
   public void testOutStream() throws Exception {
      final SinkStream stream = ObjectOutStream.stream();
      assertThat(stream.toString(), is(""));
      stream.append("t");
      final Position p1 = stream.current();
      assertThat(stream.toString(), is("t"));
      assertThat(p1.toString(), is("[1,1] 't'"));
      stream.append("es");
      final Position p2 = stream.current();
      assertThat(stream.toString(), is("tes"));
      assertThat(p2.toString(), is("[1,3] 's'"));
      stream.append("t\nx");
      final Position p3 = stream.current();
      assertThat(stream.toString(), is("test\nx"));
      assertThat(p3.toString(), is("[2,1] 'x'"));
   }

   /**
    * Tests the input stream.
    * @throws Exception if it fails
    */
   @Test
   public void testRead() throws Exception {
      final SourceStream stream = CodePointInStream.streamOf("test\nx").buffer();
      final Snapshot snapshot = new Snapshot(stream, ObjectOutStream.NONE, Environment.NONE);
      final Position start = stream.read(snapshot);
      assertThat((Integer)start.value(), is((int)'t'));
      assertThat(stream.toString(), is("t"));
      assertThat(start.toString(), is("[1,1] 't'"));
      final Position p1 = stream.read(snapshot);
      assertThat((Integer)p1.value(), is((int)'e'));
      assertThat(stream.toString(), is("te"));
      assertThat(p1.toString(), is("[1,2] 'e'"));
      final Position p2 = stream.read(snapshot);
      assertThat((Integer)p2.value(), is((int)'s'));
      assertThat(stream.toString(), is("tes"));
      assertThat(p2.toString(), is("[1,3] 's'"));
      final Position p3 = stream.read(snapshot);
      assertThat((Integer)p3.value(), is((int)'t'));
      assertThat(stream.toString(), is("test"));
      assertThat(p3.toString(), is("[1,4] 't'"));
      final Position p4 = stream.read(snapshot);
      assertThat((Integer)p4.value(), is((int)'\n'));
      assertThat(stream.toString(), is("test\n"));
      assertThat(p4.toString(), is("[1,5] '\\n'"));
      final Position p5 = stream.read(snapshot);
      assertThat((Integer)p5.value(), is((int)'x'));
      assertThat(stream.toString(), is("test\nx"));
      assertThat(p5.toString(), is("[2,1] 'x'"));
      try {
         stream.read(snapshot);
         fail("should throw on EOF");
      } catch(final ResolveException e) {
         // pass
      }
   }

   /**
    * Tests the buffered input.
    * @throws Exception if it fails
    */
   @Test
   public void testReadBuffer() throws Exception {
      final SourceStream stream = CodePointInStream.streamOf("test\nx").buffer();
      final Snapshot snapshot = new Snapshot(stream, ObjectOutStream.NONE, Environment.NONE);
      stream.read(snapshot);
      stream.read(snapshot);
      stream.read(snapshot);
      final SourceStream detached = stream.detach();
      final Position p3 = detached.read(snapshot);
      assertThat((Integer)p3.value(), is((int)'t'));
      assertThat(stream.toString(), is("test"));
      assertThat(p3.toString(), is("[1,4] 't'"));
      final Position p4 = detached.read(snapshot);
      assertThat((Integer)p4.value(), is((int)'\n'));
      assertThat(stream.toString(), is("test\n"));
      assertThat(p4.toString(), is("[1,5] '\\n'"));
      final Position p5 = detached.read(snapshot);
      assertThat((Integer)p5.value(), is((int)'x'));
      assertThat(stream.toString(), is("test\nx"));
      assertThat(p5.toString(), is("[2,1] 'x'"));
      try {
         detached.read(snapshot);
         fail("should throw on EOF");
      } catch(final ResolveException e) {
         // pass
      }
      stream.resume();
      assertThat(stream.toString(), is("tes"));
      final Position p32 = stream.read(snapshot);
      assertThat((Integer)p32.value(), is((int)'t'));
      assertThat(stream.toString(), is("test"));
      assertThat(p32.toString(), is("[1,4] 't'"));
      final Position p42 = stream.read(snapshot);
      assertThat((Integer)p42.value(), is((int)'\n'));
      assertThat(stream.toString(), is("test\n"));
      assertThat(p42.toString(), is("[1,5] '\\n'"));
      final Position p52 = stream.read(snapshot);
      assertThat((Integer)p52.value(), is((int)'x'));
      assertThat(stream.toString(), is("test\nx"));
      assertThat(p52.toString(), is("[2,1] 'x'"));
      try {
         stream.read(snapshot);
         fail("should throw on EOF");
      } catch(final ResolveException e) {
         // pass
      }
   }

   /**
    * Tests marking the input.
    * @throws Exception if it fails
    */
   @Test
   public void testReadMark() throws Exception {
      final SourceStream stream = CodePointInStream.streamOf("test\nx");
      final Snapshot snapshot = new Snapshot(stream, ObjectOutStream.NONE, Environment.NONE);
      stream.read(snapshot);
      stream.read(snapshot);
      final SourceStream detach = stream.detach();
      detach.read(snapshot);
      final SourceStream mark = detach.buffer();
      final Position p3 = mark.read(snapshot);
      assertThat((Integer)p3.value(), is((int)'t'));
      assertThat(mark.toString(), is("t"));
      assertThat(p3.toString(), is("[1,4] 't'"));
      final Position p4 = mark.read(snapshot);
      assertThat((Integer)p4.value(), is((int)'\n'));
      assertThat(mark.toString(), is("t\n"));
      assertThat(p4.toString(), is("[1,5] '\\n'"));
      final Position p5 = mark.read(snapshot);
      assertThat((Integer)p5.value(), is((int)'x'));
      assertThat(mark.toString(), is("t\nx"));
      assertThat(p5.toString(), is("[2,1] 'x'"));
      try {
         mark.read(snapshot);
         fail("should throw on EOF");
      } catch(final ResolveException e) {
         // pass
      }
      stream.attach(detach);
   }
}
