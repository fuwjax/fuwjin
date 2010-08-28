package org.fuwjin.pogo;

import org.fuwjin.postage.Failure;

public interface Position {
   Position advance(int low, int high);

   void assertSuccess() throws PogoException;

   BufferedPosition buffered();

   Memo createMemo(String name, Object value);

   void fail(String reason, Failure cause);

   boolean isAfter(Position position);

   boolean isSuccess();

   Memo memo();

   Memo releaseMemo(Memo newMemo);

   void success();

   BufferedPosition unbuffered();
}
