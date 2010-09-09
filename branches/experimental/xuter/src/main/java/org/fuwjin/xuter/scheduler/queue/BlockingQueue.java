package org.fuwjin.xuter.scheduler.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> extends SimpleQueue<T> {
   private final Lock lock = new ReentrantLock();
   private final Condition signal = lock.newCondition();
   private volatile boolean readOnly;

   @Override
   public Node<T> current() {
      try {
         Node<T> curr = super.current();
         if(curr.next() == null) {
            lock.lock();
            try {
               curr = super.current();
               while(curr.next() == null && !readOnly) {
                  signal.await();
                  curr = super.current();
               }
            } finally {
               lock.unlock();
            }
         }
         return curr;
      } catch(final InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new IllegalStateException(e);
      }
   }

   @Override
   public void enqueue(final T t) {
      if(readOnly) {
         throw new IllegalStateException();
      }
      super.enqueue(t);
      lock.lock();
      try {
         signal.signal();
      } finally {
         lock.unlock();
      }
   }

   public boolean isReadOnly() {
      return readOnly;
   }

   public void setReadOnly() {
      readOnly = true;
      lock.lock();
      try {
         signal.signalAll();
      } finally {
         lock.unlock();
      }
   }
}