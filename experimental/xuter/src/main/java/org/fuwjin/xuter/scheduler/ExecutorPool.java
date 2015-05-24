package org.fuwjin.xuter.scheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.fuwjin.xuter.scheduler.queue.BlockingQueue;

public class ExecutorPool extends AbstractExecutorService {
   private final class WorkerThread extends Thread {
      @Override
      public void run() {
         try {
            while(!isInterrupted()) {
               final Runnable next = queue.dequeue();
               if(next == null) {
                  return;
               }
               next.run();
            }
         } catch(final NoSuchElementException e) {
            // shutdown
         } finally {
            terminated.countDown();
         }
      }
   }

   private final BlockingQueue<Runnable> queue;
   private final Thread[] threads;
   private final CountDownLatch terminated;

   public ExecutorPool(final int count) {
      queue = new BlockingQueue<Runnable>();
      terminated = new CountDownLatch(count);
      threads = new Thread[count];
      for(int i = 0; i < count; i++) {
         threads[i] = new WorkerThread();
         threads[i].start();
      }
   }

   @Override
   public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
      return terminated.await(timeout, unit);
   }

   @Override
   public void execute(final Runnable command) {
      if(command == null) {
         throw new NullPointerException();
      }
      try {
         queue.enqueue(command);
      } catch(final IllegalStateException e) {
         throw new RejectedExecutionException(e);
      }
   }

   @Override
   public boolean isShutdown() {
      return queue.isReadOnly();
   }

   @Override
   public boolean isTerminated() {
      return isShutdown() && queue.isEmpty();
   }

   @Override
   public void shutdown() {
      queue.setReadOnly();
   }

   @Override
   public List<Runnable> shutdownNow() {
      shutdown();
      for(final Thread t: threads) {
         t.interrupt();
      }
      final List<Runnable> list = new LinkedList<Runnable>();
      while(!queue.isEmpty()) {
         final Runnable next = queue.dequeue();
         list.add(next);
      }
      return list;
   }
}
