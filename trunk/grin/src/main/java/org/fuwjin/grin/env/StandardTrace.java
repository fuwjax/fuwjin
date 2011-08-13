package org.fuwjin.grin.env;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.script.Bindings;
import javax.script.SimpleBindings;
import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.Expression;
import org.fuwjin.chessur.expression.ResolveException;
import org.fuwjin.dinah.Adapter;

public class StandardTrace implements Trace {
   private final AbstractSource input;
   private final AbstractSink output;
   private final Writer log;
   private Deque<Bindings> scope = new ArrayDeque<Bindings>();

   public StandardTrace(final Reader in, final Writer out, final Bindings env, final Writer logger) {
      input = new AbstractSource(in);
      output = new AbstractSink(out);
      scope.push(env);
      log = logger;
   }

   private StandardTrace(final AbstractSink out, final StandardTrace parent) {
      output = out;
      input = parent.input;
      scope = parent.scope;
      log = parent.log;
   }

   private StandardTrace(final AbstractSource in, final StandardTrace parent) {
      input = in;
      output = parent.output;
      scope = parent.scope;
      log = parent.log;
   }

   @Override
   public AbortedException abort(final String pattern, final Object... args) {
      return new AbortedException(input.summary(), output.summary(), pattern, args);
   }

   @Override
   public AbortedException abort(final Throwable cause, final String pattern, final Object... args) {
      if(cause instanceof AbortedException) {
         ((AbortedException)cause).append(input.summary(), output.summary(), pattern, args);
         return (AbortedException)cause;
      }
      return new AbortedException(input.summary(), output.summary(), cause, pattern, args);
   }

   @Override
   public void accept() throws ResolveException {
      try {
         input.read();
      } catch(final IOException e) {
         throw fail(e, "unexpected EOF");
      }
   }

   @Override
   public ResolveException fail(final String pattern, final Object... args) {
      return new ResolveException(input.summary(), output.summary(), pattern, args);
   }

   @Override
   public ResolveException fail(final Throwable cause, final String pattern, final Object... args) {
      if(cause instanceof ResolveException) {
         ((ResolveException)cause).append(input.summary(), output.summary(), pattern, args);
         return (ResolveException)cause;
      }
      return new ResolveException(input.summary(), output.summary(), cause, pattern, args);
   }

   @Override
   public Object get(final String name) {
      for(final Bindings bindings: scope) {
         if(bindings.containsKey(name)) {
            return bindings.get(name);
         }
      }
      return Adapter.UNSET;
   }

   @Override
   public void log(final Object value) throws AbortedException {
      try {
         log.append(String.valueOf(value)).append('\n');
      } catch(final IOException e) {
         throw abort(e, "could not log %s", value);
      }
   }

   @Override
   public Trace newInput(final String in) {
      final AbstractSource tempInput = new AbstractSource(new StringReader(in));
      return new StandardTrace(tempInput, this);
   }

   @Override
   public Trace newOutput() {
      final StringWriter writer = new StringWriter();
      final AbstractSink out = new AbstractSink(writer);
      return new StandardTrace(out, this) {
         @Override
         public String toString() {
            return writer.toString();
         }
      };
   }

   @Override
   public int next() throws ResolveException {
      try {
         return input.next();
      } catch(final IOException e) {
         throw fail(e, "unexpected EOF");
      }
   }

   @Override
   public void publish(final Object value) throws AbortedException {
      try {
         output.append(value);
      } catch(final IOException e) {
         throw abort(e, "could not publish %s", value);
      }
   }

   @Override
   public void put(final String name, final Object value) {
      scope.peek().put(name, value);
   }

   @Override
   public Object resolve(final Expression expression) throws AbortedException, ResolveException {
      final int iMark = input.mark();
      final int iLine = input.line();
      final int iCol = input.column();
      final int oMark = output.mark();
      final int oLine = output.line();
      final int oCol = output.column();
      try {
         return expression.resolve(this);
      } catch(final ResolveException e) {
         input.seek(iMark, iLine, iCol);
         output.seek(oMark, oLine, oCol);
         throw e;
      } finally {
         release(input, iMark);
         release(output, oMark);
      }
   }

   @Override
   public Object resolve(final String name, final Expression expression) throws AbortedException, ResolveException {
      final Object iSummary = input.summary();
      final Object oSummary = output.summary();
      scope.push(new SimpleBindings());
      try {
         return expression.resolve(this);
      } catch(final AbortedException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } catch(final ResolveException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } finally {
         scope.pop();
      }
   }

   @Override
   public Object resolveAndRevert(final Expression expression) throws AbortedException, ResolveException {
      final int iMark = input.mark();
      final int iLine = input.line();
      final int iCol = input.column();
      final int oMark = output.mark();
      final int oLine = output.line();
      final int oCol = output.column();
      try {
         return expression.resolve(this);
      } finally {
         input.seek(iMark, iLine, iCol);
         output.seek(oMark, oLine, oCol);
         release(input, iMark);
         release(output, oMark);
      }
   }

   @Override
   public Object resolveMatch(final String name, final Expression expression) throws AbortedException, ResolveException {
      final Object iSummary = input.summary();
      final Object oSummary = output.summary();
      scope.push(new SimpleBindings());
      final int mark = input.mark();
      put("match", new Object() {
         @Override
         public String toString() {
            return input.substring(mark);
         }
      });
      try {
         return expression.resolve(this);
      } catch(final AbortedException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } catch(final ResolveException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } finally {
         scope.pop();
         release(input, mark);
      }
   }

   protected void release(final IoInfo io, final int mark) throws AbortedException {
      try {
         io.release(mark);
      } catch(final IOException e) {
         throw abort(e, "Could not release io");
      }
   }
}
