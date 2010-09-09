package org.fuwjin.buffer;

import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BufferedStorage {
   private volatile CountDownLatch latch = null;
   private final ConcurrentMap<String, BlockingQueue<Invocation>> storage = new ConcurrentHashMap<String, BlockingQueue<Invocation>>();
   private final long timeout;
   private final TimeUnit unit;

   public BufferedStorage(final long timeout, final TimeUnit unit) {
      this.timeout = timeout;
      this.unit = unit;
   }

   private void await() throws InterruptedException, TimeoutException {
      if(latch != null && !latch.await(timeout, unit)) {
         throw new TimeoutException();
      }
   }

   public void expect(final int count) {
      try {
         await();
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
      latch = new CountDownLatch(count);
   }

   private BlockingQueue<Invocation> getInvocations(final Method method) {
      BlockingQueue<Invocation> invocations = storage.get(method.getName());
      if(invocations == null) {
         invocations = new LinkedBlockingQueue<Invocation>();
         final BlockingQueue<Invocation> old = storage.putIfAbsent(method.getName(), invocations);
         if(old != null) {
            invocations = old;
         }
      }
      return invocations;
   }

   public Invocation next(final Method method) throws InterruptedException, TimeoutException {
      await();
      final Invocation i = getInvocations(method).poll(timeout, unit);
      if(i == null) {
         throw new TimeoutException("No invocation occured within the timeout");
      }
      return i;
   }

   public void store(final Method method, final Invocation invocation) {
      final BlockingQueue<Invocation> invocations = getInvocations(method);
      invocations.add(invocation);
   }
}
