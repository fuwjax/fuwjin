package org.fuwjin.grin.env;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Logger;
import org.fuwjin.dinah.Adapter;

public class StandardEnv {
   public static final AbstractSink NO_SINK = new AbstractSink() {
      @Override
      public void append(final Object value) {
         // don't append
      }

      @Override
      protected void appendImpl(final String value) throws IOException {
         // don't append
      }
   };
   public static final AbstractSource NO_SOURCE = new AbstractSource() {
      @Override
      protected int readChar() throws IOException {
         return Source.EOF;
      }
   };
   public static final AbstractScope NO_SCOPE = new AbstractScope(null) {
      @Override
      public void put(final String variable, final Object value) {
         // do nothing
      }

      @Override
      protected boolean containsImpl(final String name) {
         return true;
      }

      @Override
      protected Object getImpl(final String name) {
         return Adapter.UNSET;
      }

      @Override
      protected AbstractScope newScope() {
         return new StandardScope(this);
      }
   };

   public static Source acceptFrom(final InputStream input) {
      return new AbstractSource() {
         @Override
         protected int readChar() throws IOException {
            return input.read();
         }
      };
   }

   public static Source acceptFrom(final Reader input) {
      return new AbstractSource() {
         @Override
         protected int readChar() throws IOException {
            return input.read();
         }
      };
   }

   public static Sink logTo(final Logger log) {
      return new AbstractSink() {
         @Override
         protected void appendImpl(final String value) {
            log.info(value);
         }
      };
   }

   public static Sink logTo(final PrintStream log) {
      return new AbstractSink() {
         @Override
         protected void appendImpl(final String value) {
            log.println(value);
         }
      };
   }

   public static Sink logTo(final Writer log) {
      return new AbstractSink() {
         @Override
         protected void appendImpl(final String value) throws IOException {
            log.append(value).append('\n');
         }
      };
   }

   public static Trace newTrace(final Source in, final Sink out, final Scope env, final Sink logger) {
      return new StandardTrace((AbstractSource)in, (AbstractSink)out, (AbstractScope)env, logger);
   }

   public static Sink publishTo(final PrintStream output) {
      return new AbstractSink() {
         @Override
         protected void appendImpl(final String value) {
            output.print(value);
         }
      };
   }

   public static Sink publishTo(final Writer output) {
      return new AbstractSink() {
         @Override
         protected void appendImpl(final String value) throws IOException {
            output.append(value);
         }
      };
   }

   public static Scope withState(final Map<String, ? extends Object> environment) {
      return new StandardScope(environment);
   }
}
