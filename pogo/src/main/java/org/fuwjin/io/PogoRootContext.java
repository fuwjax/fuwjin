package org.fuwjin.io;

public interface PogoRootContext {
   void accept(PogoContext childContext);

   void accept(PogoContext childContext, int ch);

   void accept(PogoContext childContext, int start, int end);

   void assertSuccess() throws PogoException;

   boolean hasRemaining();

   boolean isSuccess();

   PogoContext newChild(String name);

   Position position();

   void seek(Position mark);

   String substring(int position);

   void success(PogoException failure);
}
