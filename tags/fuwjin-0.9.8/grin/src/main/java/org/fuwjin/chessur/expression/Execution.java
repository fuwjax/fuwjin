package org.fuwjin.chessur.expression;

import static org.fuwjin.grin.env.StandardEnv.NO_SCOPE;
import static org.fuwjin.grin.env.StandardEnv.NO_SINK;
import static org.fuwjin.grin.env.StandardEnv.NO_SOURCE;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import org.fuwjin.chessur.Script;
import org.fuwjin.dinah.adapter.StandardAdapter;
import org.fuwjin.grin.env.Scope;
import org.fuwjin.grin.env.Sink;
import org.fuwjin.grin.env.Source;
import org.fuwjin.grin.env.StandardEnv;

public class Execution implements Script {
   public static Script acceptFrom(final Executable exec, final Source input) {
      return new Execution(exec, input, NO_SINK, NO_SINK, NO_SCOPE);
   }

   public static Object exec(final Executable exec) throws ExecutionException {
      return new Execution(exec, NO_SOURCE, NO_SINK, NO_SINK, NO_SCOPE).exec();
   }

   public static Script logTo(final Executable exec, final Sink log) {
      return new Execution(exec, NO_SOURCE, NO_SINK, log, NO_SCOPE);
   }

   public static Script publishTo(final Executable exec, final Sink output) {
      return new Execution(exec, NO_SOURCE, output, NO_SINK, NO_SCOPE);
   }

   public static Script withState(final Executable exec, final Scope environment) {
      return new Execution(exec, NO_SOURCE, NO_SINK, NO_SINK, environment);
   }

   private final Executable exec;
   private Source in;
   private Sink out;
   private Sink logger;
   private Scope env;

   private Execution(final Executable exec, final Source input, final Sink output, final Sink log, final Scope scope) {
      this.exec = exec;
      in = input;
      out = output;
      logger = log;
      env = scope;
   }

   @Override
   public Script acceptFrom(final InputStream input) {
      return acceptFrom(StandardEnv.acceptFrom(input));
   }

   @Override
   public Script acceptFrom(final Reader input) {
      return acceptFrom(StandardEnv.acceptFrom(input));
   }

   @Override
   public Script acceptFrom(final Source input) {
      in = input;
      return this;
   }

   @Override
   public Object exec() throws ExecutionException {
      try {
         final Object result = exec.expression().resolve(in, out, env, StandardEnv.newTrace(in, out, env, logger));
         if(StandardAdapter.isSet(result)) {
            return result;
         }
         return null;
      } catch(final AbortedException e) {
         throw new ExecutionException(String.format("Execution of %s aborted", name()), e);
      } catch(final ResolveException e) {
         throw new ExecutionException(String.format("Execution of %s failed", name()), e);
      }
   }

   @Override
   public Script logTo(final Logger log) {
      return logTo(StandardEnv.logTo(log));
   }

   @Override
   public Script logTo(final PrintStream log) {
      return logTo(StandardEnv.logTo(log));
   }

   @Override
   public Script logTo(final Sink log) {
      logger = log;
      return this;
   }

   @Override
   public Script logTo(final Writer log) {
      return logTo(StandardEnv.logTo(log));
   }

   @Override
   public String name() {
      return exec.name();
   }

   @Override
   public Script publishTo(final PrintStream output) {
      return publishTo(StandardEnv.publishTo(output));
   }

   @Override
   public Script publishTo(final Sink output) {
      out = output;
      return this;
   }

   @Override
   public Script publishTo(final Writer output) {
      return publishTo(StandardEnv.publishTo(output));
   }

   @Override
   public Script withState(final Map<String, ? extends Object> environment) {
      return withState(StandardEnv.withState(environment));
   }

   @Override
   public Script withState(final Scope environment) {
      env = environment;
      return this;
   }
}
