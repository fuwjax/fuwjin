package org.fuwjin.buffer;


public interface Buffer<T> {
   void expect(int batchSize);

   T newInterceptor();

   T newInterceptor(T target);
}
