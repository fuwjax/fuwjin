package org.fuwjin.xuter.ex;

import java.util.List;
import java.util.Observer;
import java.util.concurrent.Executor;

public class TestInterfaceImpl<T extends Observer> implements TestInterface<T> {
   private final Executor executor;
   final TestInterface<T> target;

   public TestInterfaceImpl(final Executor executor, final TestInterface<T> target) {
      this.executor = executor;
      this.target = target;
   }

   @Override
   public <E, O extends List<E>> void crazy(final O obj, final E state) throws Exception {
      target.crazy(obj, state);
   }

   @Override
   public T dontdoit(final T value, final int i) throws Exception {
      return target.dontdoit(value, i);
   }

   @Override
   public boolean equals(final Object obj) {
      if(this == obj) {
         return true;
      }
      if(obj instanceof TestInterfaceImpl) {
         final TestInterfaceImpl<?> o = (TestInterfaceImpl<?>)obj;
         return executor.equals(o.executor) && target.equals(o.target);
      }
      return false;
   }

   @Override
   public Class<? extends CharSequence> getType() {
      return target.getType();
   }

   @Override
   public int hashCode() {
      return 31 * target.hashCode() + executor.hashCode();
   }

   @Override
   public void signal() {
      executor.execute(new Runnable() {
         @Override
         public void run() {
            target.signal();
         }
      });
   }

   @Override
   public void single(final long value, final double value2) {
      executor.execute(new Runnable() {
         @Override
         public void run() {
            target.single(value, value2);
         }
      });
   }
}
