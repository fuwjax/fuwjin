package org.fuwjin.io;

public interface Position {
   Position advance(int low, int high);

   void assertSuccess() throws PogoException;

   BufferedPosition buffered();

   void fail(Position position);

   void fail(String reason, Throwable cause);

   Object fetch(String name);

   boolean isSuccess();

   void neutral();

   Object release(String name);

   void reserve(String name, Object object);

   void store(String name, Object object);

   void success();

   BufferedPosition unbuffered();
}
