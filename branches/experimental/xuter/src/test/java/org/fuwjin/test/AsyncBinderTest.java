package org.fuwjin.test;

import java.util.List;
import java.util.Observer;

import org.fuwjin.xuter.ex.TestInterface;
import org.fuwjin.xuter.scheduler.AsyncBinder;
import org.junit.Test;

public class AsyncBinderTest {
   @Test
   public void testAll() throws Exception {
      final long threadId = Thread.currentThread().getId();
      final TestInterface<Observer> o = new TestInterface<Observer>() {
         @Override
         public <E, O extends List<E>> void crazy(final O obj, final E state) throws Exception {
            System.out.println("crazy");
            if(threadId != Thread.currentThread().getId()) {
               throw new RuntimeException();
            }
         }

         @Override
         public Observer dontdoit(final Observer value, final int i) throws Exception {
            System.out.println("dontdoit");
            if(threadId != Thread.currentThread().getId()) {
               throw new RuntimeException();
            }
            return null;
         }

         @Override
         public Class<? extends CharSequence> getType() {
            System.out.println("getType");
            if(threadId != Thread.currentThread().getId()) {
               throw new RuntimeException();
            }
            return String.class;
         }

         @Override
         public void signal() {
            System.out.println("signal");
            if(threadId == Thread.currentThread().getId()) {
               throw new RuntimeException();
            }
         }

         @Override
         public void single(final long value, final double value2) {
            System.out.println("single");
            if(threadId == Thread.currentThread().getId()) {
               throw new RuntimeException();
            }
         }
      };
      final TestInterface<?> async = new AsyncBinder(3).async(TestInterface.class, o);
      async.signal();
      async.single(0, 0);
      async.crazy(null, false);
      async.dontdoit(null, 0);
      async.getType();
   }
}
