package org.fuwjin.chessur.stream;

import org.fuwjin.chessur.expression.ResolveException;

public interface SourceStream {
   void attach(SourceStream stream);

   Iterable<? extends Position> buffer(Snapshot snapshot) throws ResolveException;

   Position current();

   SourceStream detach();

   SourceStream mark();

   Position next(Snapshot snapshot) throws ResolveException;

   Position read(Snapshot snapshot) throws ResolveException;
}
