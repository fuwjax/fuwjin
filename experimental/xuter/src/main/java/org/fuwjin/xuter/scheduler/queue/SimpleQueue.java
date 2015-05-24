package org.fuwjin.xuter.scheduler.queue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleQueue<T> implements Queue<T> {
   private final AtomicReference<Node<T>> current;
   private final AtomicReference<Node<T>> tail;

   public SimpleQueue() {
      final Node<T> head = new Node<T>(null);
      current = new AtomicReference<Node<T>>(head);
      tail = new AtomicReference<Node<T>>(head);
   }

   protected Node<T> current() {
      return current.get();
   }

   @Override
   public T dequeue() throws NoSuchElementException {
      Node<T> curr;
      Node<T> next;
      do {
         curr = current();
         next = curr.next();
         if(next == null) {
            return null;
         }
      } while(!current.compareAndSet(curr, next));
      return next.t;
   }

   @Override
   public void enqueue(final T t) {
      final Node<T> task = new Node<T>(t);
      Node<T> last;
      do {
         last = tail();
      } while(!last.setNext(task));
      tail.compareAndSet(last, task);
   }

   @Override
   public boolean isEmpty() {
      return current.get().next() != null;
   }

   protected Node<T> tail() {
      Node<T> last = tail.get();
      Node<T> next = last.next();
      while(next != null) {
         tail.compareAndSet(last, next);
         last = tail.get();
         next = last.next();
      }
      return last;
   }
}
