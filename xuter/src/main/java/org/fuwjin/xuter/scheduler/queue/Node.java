package org.fuwjin.xuter.scheduler.queue;

import java.util.concurrent.atomic.AtomicReference;

class Node<T> {
   private final AtomicReference<Node<T>> next = new AtomicReference<Node<T>>();
   final T t;

   Node(final T t) {
      this.t = t;
   }

   Node<T> next() {
      return next.get();
   }

   boolean setNext(final Node<T> task) {
      return next.compareAndSet(null, task);
   }
}