package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import org.fuwjin.chessur.InStream;
import org.fuwjin.chessur.InStream.Position;
import org.fuwjin.chessur.OutStream;
import org.junit.Test;

public class InStreamDemo {
   @Test
   public void testInStream() {
      final InStream stream = InStream.streamOf("test\nx");
      final Position start = stream.start();
      assertThat(start.codePoint(), is((int)'t'));
      assertThat(start.substring(start), is(""));
      assertThat(start.toString(), is("[1,1] 't'"));
      final Position p1 = start.next();
      assertThat(p1.codePoint(), is((int)'e'));
      assertThat(p1.substring(start), is("t"));
      assertThat(p1.toString(), is("[1,2] 'e'"));
      final Position p2 = p1.next();
      assertThat(p2.codePoint(), is((int)'s'));
      assertThat(p2.substring(start), is("te"));
      assertThat(p2.toString(), is("[1,3] 's'"));
      final Position p3 = p2.next();
      assertThat(p3.codePoint(), is((int)'t'));
      assertThat(p3.substring(start), is("tes"));
      assertThat(p3.toString(), is("[1,4] 't'"));
      final Position p4 = p3.next();
      assertThat(p4.codePoint(), is((int)'\n'));
      assertThat(p4.substring(start), is("test"));
      assertThat(p4.toString(), is("[1,5] '\n'"));
      final Position p5 = p4.next();
      assertThat(p5.codePoint(), is((int)'x'));
      assertThat(p5.substring(start), is("test\n"));
      assertThat(p5.toString(), is("[2,1] 'x'"));
      final Position p6 = p5.next();
      assertThat(p6.codePoint(), is(InStream.EOF));
      assertThat(p6.substring(start), is("test\nx"));
      assertThat(p6.toString(), is("[2,2] EOF"));
      final Position p7 = p6.next();
      assertSame(p6, p7);
      final Position p2_2 = p1.next();
      assertSame(p2, p2_2);
   }

   @Test
   public void testOutStream() {
      final OutStream stream = OutStream.stream();
      final OutStream.Position start = stream.start();
      assertThat(stream.toString(), is(""));
      assertThat(start.toString(), is("[1,0] SOF"));
      final OutStream.Position p1 = start.append("t");
      assertThat(stream.toString(), is("t"));
      assertThat(p1.toString(), is("[1,1] 't'"));
      final OutStream.Position p2 = p1.append("es");
      assertThat(stream.toString(), is("tes"));
      assertThat(p2.toString(), is("[1,3] 's'"));
      final OutStream.Position p3 = p2.append("t\nx");
      assertThat(stream.toString(), is("test\nx"));
      assertThat(p3.toString(), is("[2,1] 'x'"));
   }
}
