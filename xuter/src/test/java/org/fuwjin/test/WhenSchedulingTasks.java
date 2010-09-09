package org.fuwjin.test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fuwjin.buffer.BufferFactory.newBuffer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.fuwjin.buffer.Buffer;
import org.fuwjin.buffer.Invocation;
import org.fuwjin.xuter.scheduler.AsyncBinder;
import org.fuwjin.xuter.scheduler.ExecutorPool;
import org.fuwjin.xuter.scheduler.ScheduledExecutor;
import org.junit.Before;
import org.junit.Test;

public class WhenSchedulingTasks {
   public interface TestInterface {
      void voidOneArg(String arg);
   }

   public interface TestInterface2 {
      void voidNoArg();
   }

   public interface TestInterface3 {
      Object objectOneArg(String arg);
   }

   public interface TestInterfaceBuffer extends Buffer<TestInterface> {
      String voidOneArg();
   }

   public interface TestInterfaceBuffer2 extends Buffer<TestInterface2> {
      Invocation voidNoArg();
   }

   public static class TestObject3 implements TestInterface3 {
      @Override
      public Object objectOneArg(final String arg) {
         return arg;
      }
   }

   private Executor scheduler;
   private AsyncBinder binder;

   @Before
   public void setup() {
      scheduler = new ScheduledExecutor(new ExecutorPool(3));
      binder = new AsyncBinder(3);
   }

   @Test
   public void shouldBind() {
      final TestInterfaceBuffer buffer = newBuffer(TestInterfaceBuffer.class);
      final TestInterface test = binder.async(TestInterface.class, buffer.newInterceptor());
      test.voidOneArg("test");
      assertThat(buffer.voidOneArg(), is("test"));
   }

   @Test
   public void shouldBind2() {
      final TestInterfaceBuffer2 buffer = newBuffer(TestInterfaceBuffer2.class);
      final TestInterface2 test = binder.async(TestInterface2.class, buffer.newInterceptor());
      test.voidNoArg();
      assertThat(buffer.voidNoArg(), is(notNullValue()));
   }

   @Test
   public void shouldBuffer() {
      final TestInterfaceBuffer buffer = newBuffer(TestInterfaceBuffer.class);
      final TestInterface test = buffer.newInterceptor();
      test.voidOneArg("test");
      assertThat(buffer.voidOneArg(), is("test"));
   }

   @Test
   public void shouldNotBindReturn() {
      final TestInterface3 obj = new TestObject3();
      final TestInterface3 test = binder.async(TestInterface3.class, obj);
      assertThat(test.objectOneArg("test"), is((Object)"test"));
   }

   @Test
   public void shouldSchedule() throws InterruptedException {
      final CountDownLatch latch = new CountDownLatch(1);
      scheduler.execute(new Runnable() {
         @Override
         public void run() {
            latch.countDown();
         }
      });
      assertTrue(latch.await(30, MILLISECONDS));
   }
}
