package org.fuwjin.pogo;


public interface Position {
   Position advance(int low, int high);

   void assertSuccess() throws PogoException;

   BufferedPosition buffered();

   Memo createMemo(String name, Object value);

   void fail(String reason, Throwable cause);

   boolean isAfter(Position position);

   boolean isSuccess();

   Memo memo();

   Memo releaseMemo(Memo newMemo);

   void success();

   BufferedPosition unbuffered();
}
