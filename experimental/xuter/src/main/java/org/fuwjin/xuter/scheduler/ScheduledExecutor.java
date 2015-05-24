package org.fuwjin.xuter.scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fuwjin.xuter.scheduler.queue.Queue;
import org.fuwjin.xuter.scheduler.queue.SimpleQueue;

public class ScheduledExecutor implements Executor {
   private class Worker implements Runnable {
      @Override
      public void run() {
         if(lock.tryLock()) {
            try {
               while(!Thread.currentThread().isInterrupted()) {
                  while(interrupts.get() > 0) {
                     interrupted.await();
                  }
                  final Runnable next = queue.dequeue();
                  if(next == null) {
                     return;
                  }
                  next.run();
               }
            } catch(final InterruptedException e) {
               Thread.currentThread().interrupt();
            } finally {
               lock.unlock();
            }
            scheduled = false;
            if(!queue.isEmpty()) {
               schedule();
            }
         }
      }
   }

   private final Executor executor;
   private final Queue<Runnable> queue;
   private volatile boolean scheduled;
   private final Lock lock = new ReentrantLock();
   private final AtomicInteger interrupts = new AtomicInteger();
   private final Condition interrupted = lock.newCondition();

   public ScheduledExecutor(final Executor executor) {
      this.executor = executor;
      queue = new SimpleQueue<Runnable>();
   }

   @Override
   public void execute(final Runnable command) {
      queue.enqueue(command);
      schedule();
   }

   public void preempt(final Runnable command) throws InterruptedException {
      interrupts.incrementAndGet();
      lock.lockInterruptibly();
      try {
         command.run();
         interrupts.decrementAndGet();
         interrupted.signalAll();
      } finally {
         lock.unlock();
      }
   }

   private void schedule() {
      if(!scheduled) {
         scheduled = true;
         executor.execute(new Worker());
      }
   }
}
