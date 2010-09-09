package org.fuwjin.xuter.ex;

import java.util.List;
import java.util.Observer;

public interface TestInterface<T extends Observer> {
   <E, O extends List<E>> void crazy(final O obj, final E state) throws Exception;

   T dontdoit(T value, int i) throws Exception;

   Class<? extends CharSequence> getType();

   void signal();

   void single(long value, double value2);
}
