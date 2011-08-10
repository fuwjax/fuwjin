package org.fuwjin.grin.env;

import org.fuwjin.chessur.expression.AbortedException;

public interface IoInfo {
   public abstract int column();

   public abstract int line();

   public abstract int mark();

   public abstract void release(final int mark) throws AbortedException;

   public abstract void seek(final int mark, final int line, final int column);

   public abstract Object summary();
}
