package org.fuwjin.xuter.scheduler.queue;


public interface Queue<E> {
   E dequeue() throws IllegalStateException;

   void enqueue(E e) throws IllegalStateException;

   boolean isEmpty();
}
