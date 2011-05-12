/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests the comparative performance of exceptions, sentinels and result
 * objects. There are several static variables which can be tweaked to alter the
 * test. This microbenchmark is discussed on
 * http://blog.fuwjax.org/2011/04/java-exception-microbenchmark/
 */
public class ExceptionTest {
   /**
    * The exception used by the Exception TestStrategy.
    */
   private static class NameException extends RuntimeException {
      private static final long serialVersionUID = 1L;

      public NameException(final String message) {
         super(message);
      }
   }

   /**
    * The potentially failing callback. This enum attempts to generate a name of
    * at least length characters long, but it may abort earlier subject to the
    * factory strategy.
    */
   public enum NameFactory implements Namer<String> {
      /**
       * Aborts name generation if a bad character is produced or the required
       * length is reached. The rate at which the aborts are caused by bad
       * characters is roughly given by FAILURE_RATE.
       */
      Bounded {
         @Override
         protected boolean check(final int length, final StringBuilder builder, final char ch) {
            return BAD_CHARS.indexOf(ch) == -1 && builder.length() < length;
         }
      },
      /**
       * Aborts name generation when a bad character is produced. The rate at
       * which the aborts are shorter than the required length is roughly given
       * by FAILURE_RATE. This strategy tests the situation when failures may
       * take substantially less time than successes. However, as the success
       * name length is unbounded, it's very difficult to compare the various
       * TestStrategies when using this factory.
       */
      Unbounded {
         @Override
         protected boolean check(final int length, final StringBuilder builder, final char ch) {
            return BAD_CHARS.indexOf(ch) == -1;
         }
      },
      /**
       * Always generates a successful name. Used to test the theory that
       * try/catch is expensive even without throwing exceptions.
       */
      Success {
         @Override
         protected boolean check(final int length, final StringBuilder builder, final char ch) {
            return builder.length() < length;
         }

         @Override
         public double failureRate() {
            return 0;
         }
      },
      /**
       * Always generates a failure, assuming the length is greater than 0. Used
       * to test the theory that 100% failure is very expensive for exceptions.
       */
      Failure {
         @Override
         protected boolean check(final int length, final StringBuilder builder, final char ch) {
            builder.setLength(0);
            return false;
         }

         @Override
         public double failureRate() {
            return 1;
         }
      };
      private final static Random rand = new Random();
      private final static ThreadLocal<StringBuilder> localBuilder = new ThreadLocal<StringBuilder>() {
         @Override
         protected StringBuilder initialValue() {
            return new StringBuilder(VALID_LENGTH);
         }
      };

      protected abstract boolean check(int length, StringBuilder builder, char ch);

      @Override
      public String createName(final int length) {
         final StringBuilder builder = localBuilder.get();
         builder.setLength(0);
         char ch;
         do {
            final int index = rand.nextInt(CHARS.length());
            ch = CHARS.charAt(index);
            builder.append(ch);
         } while(check(length, builder, ch));
         return builder.toString();
      }

      /**
       * The predicted rate of failures. Used to compare against the strategies
       * to assure they are counting failures correctly.
       * @return the failure rate
       */
      public double failureRate() {
         return 1 - Math.pow(1. - BAD_CHARS.length() / (double)CHARS.length(), VALID_LENGTH - 1);
      }
   }

   /**
    * The exception used by the NoTrace TestStrategy.
    */
   private static class NameNoTraceException extends RuntimeException {
      private static final long serialVersionUID = 1L;

      public NameNoTraceException(final String message) {
         super(message);
      }

      @Override
      public synchronized Throwable fillInStackTrace() {
         return this;
      }
   }

   /**
    * The NameFactory interface. Each TestStrategy defines a Namer decorator
    * used to validate the NameFactory in a way peculiar to the strategy.
    * @param <T> the return type
    */
   public interface Namer<T> {
      /**
       * Creates a new name.
       * @param length the required length of the name
       * @return the name object, usually a String, though the TestStrategy may
       *         require otherwise
       */
      T createName(int length);
   }

   /**
    * The result object and sentinel value. The object is constructed through
    * one of the static methods.
    */
   public static class NameResult {
      /**
       * Creates a failure instance. Used by both the Result and Sentinel
       * TestStrategy.
       * @param message the failure message
       * @return the result object/sentinel
       */
      public static NameResult failure(final String message) {
         return new NameResult(null, message);
      }

      /**
       * Creates a success instance. Used by the Result TestStrategy.
       * @param name the valid name
       * @return the result object
       */
      public static NameResult success(final String name) {
         return new NameResult(name, null);
      }

      private final String name;
      private final String message;

      private NameResult(final String name, final String message) {
         this.name = name;
         this.message = message;
      }

      /**
       * Returns the failure message.
       * @return the failure message, null if a successful result object
       */
      public String getMessage() {
         return message;
      }

      /**
       * Returns the validated name.
       * @return the validated name, null if a failure result object or sentinel
       */
      public String getName() {
         return name;
      }

      /**
       * Returns whether the result object marks a success.
       * @return true if a success result object, false if a failure result
       *         object or sentinel
       */
      public boolean isSuccess() {
         return name != null;
      }
   }

   private static class TestBatch implements Callable<Object> {
      private final TestStrategy strategy;
      private final Namer<?> namer;

      /**
       * Creates a new TimedTest instance.
       * @param namer the base name factory
       * @param strategy the strategy executed during the batch.
       */
      public TestBatch(final Namer<String> namer, final TestStrategy strategy) {
         this.namer = strategy.createNamer(namer);
         this.strategy = strategy;
      }

      /**
       * Executes a set of batch
       */
      @Override
      public Object call() {
         final AtomicReference<String> success = new AtomicReference<String>();
         final AtomicReference<String> failure = new AtomicReference<String>();
         int diff = 0;
         final long start = System.nanoTime();
         for(int i = 0; i < BATCH_SIZE; i++) {
            diff += strategy.perform(namer, success, failure, VALID_LENGTH);
         }
         final long duration = (System.nanoTime() - start) / BATCH_SIZE;
         final double failureRate = (BATCH_SIZE - diff) / 2. / BATCH_SIZE;
         strategy.addSample(duration, failureRate);
         return null;
      }
   }

   private enum TestStrategy {
      /**
       * The Control test strategy. Attempts to perform the as many as possible
       * of the similar operations as the rest of the TestStrategy. As part of
       * this limitation, the strategy does not attempt to distinguish success
       * or failure.
       */
      Control {
         /**
          * This strategy does not test for failure, so the failure rate is not
          * available.
          */
         @Override
         protected double averageFailureRate() {
            return Double.NaN;
         }

         @Override
         public Namer<?> createNamer(final Namer<String> namer) {
            return new Namer<String>() {
               @Override
               public String createName(final int length) {
                  final String name = namer.createName(length);
                  if(name.length() < length) {
                     return name + " is not a valid name";
                  }
                  return name;
               }
            };
         }

         @Override
         public int perform(final Namer<?> namer, final AtomicReference<String> success,
               final AtomicReference<String> failure, final int length) {
            final String name = ((Namer<String>)namer).createName(length);
            success.set(name);
            return 1;
         }
      },
      /**
       * The Detect test strategy. Attempts to only detect success or failure
       * but not do any reporting. Allows the theory about StringBuilder expense
       * to be tested.
       */
      Detect {
         @Override
         public Namer<?> createNamer(final Namer<String> namer) {
            return new Namer<String>() {
               @Override
               public String createName(final int length) {
                  final String name = namer.createName(length);
                  if(name.length() < length) {
                     return null;
                  }
                  return name;
               }
            };
         }

         @Override
         public int perform(final Namer<?> namer, final AtomicReference<String> success,
               final AtomicReference<String> failure, final int length) {
            final String name = ((Namer<String>)namer).createName(length);
            if(name != null) {
               success.set(name);
               return 1;
            }
            failure.set(name);
            return -1;
         }
      },
      /**
       * The Exception test strategy. The namer for this strategy throws an
       * exception on failure. The perform uses a standard try/catch block.
       */
      Exception {
         @Override
         public Namer<?> createNamer(final Namer<String> namer) {
            return new Namer<String>() {
               @Override
               public String createName(final int length) {
                  final String name = namer.createName(length);
                  if(name.length() < length) {
                     throw new NameException(name + " is not a valid name");
                  }
                  return name;
               }
            };
         }

         @Override
         public int perform(final Namer<?> namer, final AtomicReference<String> success,
               final AtomicReference<String> failure, final int length) {
            try {
               final String name = ((Namer<String>)namer).createName(length);
               success.set(name);
               return 1;
            } catch(final NameException e) {
               failure.set(e.getMessage());
               return -1;
            }
         }
      },
      /**
       * The NoTrace test strategy. The namer for this strategy throws an
       * exception on failure. The perform uses a standard try/catch block. The
       * distinguishing characteristic between this TestStrategy and Exception
       * si that the exception thrown by this Namer has disabled
       * fillInStackTrace().
       */
      NoTrace {
         @Override
         public Namer<?> createNamer(final Namer<String> namer) {
            return new Namer<String>() {
               @Override
               public String createName(final int length) {
                  final String name = namer.createName(length);
                  if(name.length() < length) {
                     throw new NameNoTraceException(name + " is not a valid name");
                  }
                  return name;
               }
            };
         }

         @Override
         public int perform(final Namer<?> namer, final AtomicReference<String> success,
               final AtomicReference<String> failure, final int length) {
            try {
               final String name = ((Namer<String>)namer).createName(length);
               success.set(name);
               return 1;
            } catch(final NameNoTraceException e) {
               failure.set(e.getMessage());
               return -1;
            }
         }
      },
      /**
       * The Result test strategy. The namer produces a NameResult on both
       * success and failure. The provide method checks isSuccess() and then
       * calls either getName() or getMessage().
       */
      Result {
         @Override
         public Namer<?> createNamer(final Namer<String> namer) {
            return new Namer<NameResult>() {
               @Override
               public NameResult createName(final int length) {
                  final String name = namer.createName(length);
                  if(name.length() < length) {
                     return NameResult.failure(name + " is not a valid name");
                  }
                  return NameResult.success(name);
               }
            };
         }

         @Override
         public int perform(final Namer<?> namer, final AtomicReference<String> success,
               final AtomicReference<String> failure, final int length) {
            final NameResult result = ((Namer<NameResult>)namer).createName(length);
            if(result.isSuccess()) {
               success.set(result.getName());
               return 1;
            }
            failure.set(result.getMessage());
            return -1;
         }
      },
      /**
       * The Sentinel test strategy. The namer produces a NameResult on failure
       * and returns the name on success. The provide method checks instanceof
       * and then casts to either a String or a NameResult depending on the
       * type.
       */
      Sentinel {
         @Override
         public Namer<?> createNamer(final Namer<String> namer) {
            return new Namer<Object>() {
               @Override
               public Object createName(final int length) {
                  final String name = namer.createName(length);
                  if(name.length() < length) {
                     return NameResult.failure(name + " is not a valid name");
                  }
                  return name;
               }
            };
         }

         @Override
         public int perform(final Namer<?> namer, final AtomicReference<String> success,
               final AtomicReference<String> failure, final int length) {
            final Object result = ((Namer<Object>)namer).createName(length);
            if(result instanceof String) {
               success.set((String)result);
               return 1;
            }
            failure.set(((NameResult)result).getMessage());
            return -1;
         }
      };
      long[] times = new long[TEST_CYCLES - WARM_UP];
      double failures;
      int samples;
      int used;

      /**
       * Adds the measurements from a TestStrategy execution batch to the sample
       * set.
       * @param duration the average nanos per TestStrategy execution
       * @param rate the percent volume of failures in the TestStrategy
       *        executions
       */
      public synchronized void addSample(final long duration, final double rate) {
         if(samples++ > WARM_UP) {
            times[used] = duration;
            failures += rate;
            used++;
         }
      }

      protected double averageFailureRate() {
         return failures / used;
      }

      public abstract Namer<?> createNamer(Namer<String> namer);

      protected Object medianTime() {
         Arrays.sort(times);
         return times[times.length / 2];
      }

      public abstract int perform(Namer<?> namer, AtomicReference<String> success, AtomicReference<String> failure,
            int length);

      @Override
      public String toString() {
         return String.format("%10s %5d  %6.3f%%", name(), medianTime(), 100 * averageFailureRate());
      }
   }

   /**
    * NameFactory instance to use as the base namer.
    */
   private final static NameFactory NAME_FACTORY = NameFactory.Bounded;
   /**
    * Thread count in the test executor pool. Setting this to more than 1 may
    * expose unoptimized cache behavior, but I haven't seen anything to back
    * that up.
    */
   private final static int THREADS = 1;
   /**
    * The set of potential name characters. Adding more characters will decrease
    * the failure rate.
    */
   private final static String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   /**
    * The set of characters which will cause the name algorithm to abort. Adding
    * more distinct characters will increase the failure rate.
    */
   private final static String BAD_CHARS = "1lI";
   /**
    * The length of a valid string. A longer length will increase the expected
    * failure rate and runtime of the TestStrategy execution.
    */
   private static final int VALID_LENGTH = 2;
   /**
    * The batch size for TestStrategy executions per test cycle. A larger batch
    * size should in theory produce more consistent results.
    */
   private static final int BATCH_SIZE = 10000;
   /**
    * The number of batches run against each TestStrategy. More batches should
    * in theory produce more consistent results.
    */
   private static final int TEST_CYCLES = 1000;
   /**
    * The number of TEST_CYCLES to omit from the statistics. More omitted
    * batches should in theory produce more consistent results, up to a point.
    * Once the JIT has kicked in and optimized the microbenchmark, excluding
    * batches is just wasting good data. But since we don't know when it will
    * kick in, waste away.
    */
   private static final int WARM_UP = 100;

   /**
    * Runs the test.
    * @param args are ignored
    * @throws InterruptedException if the test executor is interrupted
    */
   public static void main(final String... args) throws InterruptedException {
      System.out.println(String.format("Executing against the %s factory with %d threads", NAME_FACTORY, THREADS));
      System.out.println(String.format("Names are %d characters long from %d total and %d bad characters",
            VALID_LENGTH, CHARS.length(), BAD_CHARS.length()));
      System.out.println(String.format("%d batches of %d executions, excluding %d batches from stats", TEST_CYCLES,
            BATCH_SIZE, WARM_UP));
      System.out.println(String.format("Expected failure rate: %.3f%%", NAME_FACTORY.failureRate() * 100));
      System.out.println();
      final List<Callable<Object>> tests = new ArrayList<Callable<Object>>();
      for(final TestStrategy strategy: TestStrategy.values()) {
         for(int i = 0; i < TEST_CYCLES; i++) {
            tests.add(new TestBatch(NAME_FACTORY, strategy));
         }
      }
      Collections.shuffle(tests);
      final ExecutorService service = Executors.newFixedThreadPool(THREADS);
      try {
         service.invokeAll(tests);
         System.out.println("  Strategy  Time   Fail %");
         System.out.println(" ---------  ----  -------");
         for(final TestStrategy strategy: TestStrategy.values()) {
            System.out.println(strategy);
         }
      } finally {
         service.shutdown();
      }
   }
}
