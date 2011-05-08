package org.fuwjin.chessur.stream;

import org.fuwjin.chessur.expression.AbortedException;

public interface SinkStream {
   void append(Object value) throws AbortedException;

   void attach(SinkStream stream) throws AbortedException;

   Position current();

   SinkStream detach();
}
