package org.fuwjin.test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.fuwjin.xuter.scheduler.ExecutorPool;
import org.fuwjin.xuter.scheduler.ScheduledExecutor;

public class SchedulerTest {
   private static final int tCount = 1;
   private static final int sCount = 10;
   private static final int taskCount = 10000;

   public static void main(final String... args) throws Exception {
      final Random rand = new Random();
      final ExecutorPool pool = new ExecutorPool(tCount);
      final ScheduledExecutor[] schedulers = new ScheduledExecutor[sCount];
      for(int i = 0; i < sCount; i++) {
         schedulers[i] = new ScheduledExecutor(pool);
      }
      final CountDownLatch latch = new CountDownLatch(taskCount);
      final long start = System.nanoTime();
      for(int j = 0; j < taskCount; j++) {
         for(int i = 1; i < sCount; i++) {
            schedulers[i].execute(new Runnable() {
               @Override
               public void run() {
                  final int[] list = new int[1000];
                  for(int i = 0; i < 1000; i++) {
                     list[i] = rand.nextInt();
                  }
                  Arrays.sort(list);
               }
            });
         }
         schedulers[0].execute(new Runnable() {
            @Override
            public void run() {
               latch.countDown();
            }
         });
      }
      latch.await(1, TimeUnit.SECONDS);
      System.out.println(System.nanoTime() - start);
   }
}
