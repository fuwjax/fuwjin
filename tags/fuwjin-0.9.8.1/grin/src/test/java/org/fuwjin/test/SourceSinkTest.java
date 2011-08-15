package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.script.SimpleBindings;
import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.Expression;
import org.fuwjin.chessur.expression.ResolveException;
import org.fuwjin.grin.env.StandardTrace;
import org.fuwjin.grin.env.Trace;
import org.fuwjin.util.AssertThat;
import org.junit.Test;

public class SourceSinkTest {
   private final class Transfer3 implements Expression {
      @Override
      public Object resolve(final Trace trace) throws AbortedException, ResolveException {
         for(int i = 0; i < 3; i++) {
            trace.publish((char)trace.next());
            trace.accept();
         }
         return null;
      }
   }

   private final class Transfer3AndMatch implements Expression {
      @Override
      public Object resolve(final Trace trace) throws AbortedException, ResolveException {
         for(int i = 0; i < 3; i++) {
            trace.publish((char)trace.next());
            trace.accept();
         }
         assertThat(trace.get("match").toString(), is("alp"));
         return null;
      }
   }

   private final class Transfer3ThenFail implements Expression {
      @Override
      public Object resolve(final Trace trace) throws AbortedException, ResolveException {
         for(int i = 0; i < 3; i++) {
            trace.publish((char)trace.next());
            trace.accept();
         }
         throw trace.fail("oops");
      }
   }

   private final Reader source = new StringReader("alphabet");
   private final StringWriter writer = new StringWriter();
   private final Trace trace = new StandardTrace(source, writer, new StringWriter(), new SimpleBindings());

   @Test
   public void testInput() throws Exception {
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      assertThat(trace.next(), is((int)'l'));
      trace.accept();
      assertThat(trace.next(), is((int)'p'));
      trace.accept();
      assertThat(trace.next(), is((int)'h'));
      trace.accept();
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      assertThat(trace.next(), is((int)'b'));
      trace.accept();
      assertThat(trace.next(), is((int)'e'));
      trace.accept();
      assertThat(trace.next(), is((int)'t'));
      trace.accept();
      new AssertThat() {
         @Override
         public void when() throws ResolveException {
            trace.next();
         }
      }.willThrow(ResolveException.class);
      new AssertThat() {
         @Override
         public void when() throws ResolveException {
            trace.accept();
         }
      }.willThrow(ResolveException.class);
   }

   @Test
   public void testOutput() throws Exception {
      trace.publish("hi");
      assertThat(writer.toString(), is("hi"));
      trace.publish(", ");
      assertThat(writer.toString(), is("hi, "));
      trace.publish("mom");
      assertThat(writer.toString(), is("hi, mom"));
      trace.publish('!');
      assertThat(writer.toString(), is("hi, mom!"));
      trace.publish(null);
      assertThat(writer.toString(), is("hi, mom!"));
   }

   @Test
   public void testResolve() throws Exception {
      trace.resolve(new Transfer3());
      assertThat(trace.next(), is((int)'h'));
      trace.accept();
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      trace.publish("s");
      assertThat(writer.toString(), is("alps"));
   }

   @Test
   public void testResolveAndRevert() throws Exception {
      trace.resolveAndRevert(new Transfer3());
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      assertThat(trace.next(), is((int)'l'));
      trace.accept();
      trace.publish("s");
      assertThat(writer.toString(), is("s"));
   }

   @Test
   public void testResolveAndRevertFail() throws Exception {
      new AssertThat() {
         @Override
         public void when() throws AbortedException, ResolveException {
            trace.resolveAndRevert(new Transfer3ThenFail());
         }
      }.willThrow(ResolveException.class).withMessage("oops: [1,3] \"alp⎀\" -> [1,3] \"alp⎀\"");
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      assertThat(trace.next(), is((int)'l'));
      trace.accept();
      trace.publish("s");
      assertThat(writer.toString(), is("s"));
   }

   @Test
   public void testResolveFail() throws Exception {
      new AssertThat() {
         @Override
         public void when() throws Throwable {
            trace.resolve(new Transfer3ThenFail());
         }
      }.willThrow(ResolveException.class).withMessage("oops: [1,3] \"alp⎀\" -> [1,3] \"alp⎀\"");
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      assertThat(trace.next(), is((int)'l'));
      trace.accept();
      trace.publish("s");
      assertThat(writer.toString(), is("s"));
   }

   @Test
   public void testResolveMatch() throws Exception {
      trace.resolveMatch("test", new Transfer3AndMatch());
      assertThat(trace.next(), is((int)'h'));
      trace.accept();
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      trace.publish("s");
      assertThat(writer.toString(), is("alps"));
   }

   @Test
   public void testResolveMatchFail() throws Exception {
      new AssertThat() {
         @Override
         public void when() throws Exception {
            trace.resolveMatch("test", new Transfer3ThenFail());
         }
      }.willThrow(ResolveException.class).withMessage(
            "oops: [1,3] \"alp⎀\" -> [1,3] \"alp⎀\"\nin test: [1,0] \"⎀alp\" -> [1,0] \"⎀alp\"");
   }

   @Test
   public void testResolveName() throws Exception {
      trace.resolve("test", new Transfer3());
      assertThat(trace.next(), is((int)'h'));
      trace.accept();
      assertThat(trace.next(), is((int)'a'));
      trace.accept();
      trace.publish("s");
      assertThat(writer.toString(), is("alps"));
   }

   @Test
   public void testResolveNameFail() throws Exception {
      new AssertThat() {
         @Override
         public void when() throws Exception {
            trace.resolve("test", new Transfer3ThenFail());
         }
      }.willThrow(ResolveException.class).withMessage(
            "oops: [1,3] \"alp⎀\" -> [1,3] \"alp⎀\"\nin test: [1,0] \"⎀alp\" -> [1,0] \"⎀alp\"");
   }
}
