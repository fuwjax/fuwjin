package org.fuwjin.grin.env;

import org.fuwjin.chessur.expression.AbortedException;

public interface Match {
   public void release() throws AbortedException;

   @Override
   public String toString();
}
