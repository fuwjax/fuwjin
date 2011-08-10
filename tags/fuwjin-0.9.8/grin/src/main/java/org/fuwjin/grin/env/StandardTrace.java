package org.fuwjin.grin.env;

import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.Expression;
import org.fuwjin.chessur.expression.ResolveException;

public class StandardTrace implements Trace {
   private final AbstractSource input;
   private final AbstractSink output;
   private final Sink log;
   private AbstractScope scope;

   public StandardTrace(final AbstractSource in, final AbstractSink out, final AbstractScope env, final Sink logger) {
      input = in;
      output = out;
      scope = env;
      log = logger;
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
   public void append(final Object value) {
      log.append(value);
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
   public Trace newInput(final Source in) {
      return new StandardTrace((AbstractSource)in, output, scope, log);
   }

   @Override
   public Trace newOutput(final Sink out) {
      return new StandardTrace(input, (AbstractSink)out, scope, log);
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
         return expression.resolve(input, output, scope, this);
      } catch(final ResolveException e) {
         input.seek(iMark, iLine, iCol);
         output.seek(oMark, oLine, oCol);
         throw e;
      } finally {
         input.release(iMark);
         output.release(oMark);
      }
   }

   @Override
   public Object resolve(final String name, final Expression expression) throws AbortedException, ResolveException {
      final Object iSummary = input.summary();
      final Object oSummary = output.summary();
      scope = scope.newScope();
      try {
         return expression.resolve(input, output, scope, this);
      } catch(final AbortedException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } catch(final ResolveException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } finally {
         scope = scope.parent();
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
         return expression.resolve(input, output, scope, this);
      } finally {
         input.seek(iMark, iLine, iCol);
         output.seek(oMark, oLine, oCol);
         input.release(iMark);
         output.release(oMark);
      }
   }

   @Override
   public Object resolveMatch(final String name, final Expression expression) throws AbortedException, ResolveException {
      final Object iSummary = input.summary();
      final Object oSummary = output.summary();
      scope = scope.newScope();
      final Match match = input.newMatch();
      scope.put("match", match);
      try {
         return expression.resolve(input, output, scope, this);
      } catch(final AbortedException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } catch(final ResolveException e) {
         e.append(iSummary, oSummary, "in %s", name);
         throw e;
      } finally {
         match.release();
         scope = scope.parent();
      }
   }
}
