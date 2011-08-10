package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.StringReader;
import java.io.StringWriter;
import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.Expression;
import org.fuwjin.chessur.expression.ResolveException;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.StandardEnv;
import org.fuwjin.grin.env.Trace;
import org.fuwjin.util.AssertThat;
import org.junit.Test;

public class SourceSinkTest {
   private final class Transfer3 implements Expression {
      @Override
      public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
            throws AbortedException, ResolveException {
         for(int i = 0; i < 3; i++) {
            output.append((char)input.next());
            input.read();
         }
         return null;
      }
   }

   private final class Transfer3AndMatch implements Expression {
      @Override
      public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
            throws AbortedException, ResolveException {
         for(int i = 0; i < 3; i++) {
            output.append((char)input.next());
            input.read();
         }
         assertThat(scope.get("match").toString(), is("alp"));
         return null;
      }
   }

   private final class Transfer3ThenFail implements Expression {
      @Override
      public Object resolve(final Source input, final Sink output, final Scope scope, final Trace trace)
            throws AbortedException, ResolveException {
         for(int i = 0; i < 3; i++) {
            output.append((char)input.next());
            input.read();
         }
         throw trace.fail("oops");
      }
   }

   private final Source source = StandardEnv.acceptFrom(new StringReader("alphabet"));
   private final StringWriter writer = new StringWriter();
   private final Sink sink = StandardEnv.publishTo(writer);
   private final Trace trace = StandardEnv.newTrace(source, sink, StandardEnv.NO_SCOPE, StandardEnv.NO_SINK);

   @Test
   public void testInput() throws ResolveException {
      assertThat(source.next(), is((int)'a'));
      source.read();
      assertThat(source.next(), is((int)'l'));
      source.read();
      assertThat(source.next(), is((int)'p'));
      source.read();
      assertThat(source.next(), is((int)'h'));
      source.read();
      assertThat(source.next(), is((int)'a'));
      source.read();
      assertThat(source.next(), is((int)'b'));
      source.read();
      assertThat(source.next(), is((int)'e'));
      source.read();
      assertThat(source.next(), is((int)'t'));
      source.read();
      new AssertThat() {
         @Override
         public void when() throws ResolveException {
            source.next();
         }
      }.willThrow(ResolveException.class);
      new AssertThat() {
         @Override
         public void when() throws ResolveException {
            source.read();
         }
      }.willThrow(ResolveException.class);
   }

   @Test
   public void testOutput() throws Exception {
      sink.append("hi");
      assertThat(writer.toString(), is("hi"));
      sink.append(", ");
      assertThat(writer.toString(), is("hi, "));
      sink.append("mom");
      assertThat(writer.toString(), is("hi, mom"));
      sink.append('!');
      assertThat(writer.toString(), is("hi, mom!"));
      sink.append(null);
      assertThat(writer.toString(), is("hi, mom!"));
   }

   @Test
   public void testResolve() throws Exception {
      trace.resolve(new Transfer3());
      assertThat(source.next(), is((int)'h'));
      source.read();
      assertThat(source.next(), is((int)'a'));
      source.read();
      sink.append("s");
      assertThat(writer.toString(), is("alps"));
   }

   @Test
   public void testResolveAndRevert() throws Exception {
      trace.resolveAndRevert(new Transfer3());
      assertThat(source.next(), is((int)'a'));
      source.read();
      assertThat(source.next(), is((int)'l'));
      source.read();
      sink.append("s");
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
      assertThat(source.next(), is((int)'a'));
      source.read();
      assertThat(source.next(), is((int)'l'));
      source.read();
      sink.append("s");
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
      assertThat(source.next(), is((int)'a'));
      source.read();
      assertThat(source.next(), is((int)'l'));
      source.read();
      sink.append("s");
      assertThat(writer.toString(), is("s"));
   }

   @Test
   public void testResolveMatch() throws Exception {
      trace.resolveMatch("test", new Transfer3AndMatch());
      assertThat(source.next(), is((int)'h'));
      source.read();
      assertThat(source.next(), is((int)'a'));
      source.read();
      sink.append("s");
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
      assertThat(source.next(), is((int)'h'));
      source.read();
      assertThat(source.next(), is((int)'a'));
      source.read();
      sink.append("s");
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
