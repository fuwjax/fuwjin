package org.fuwjin.chessur;

public interface ICatalog extends IExecutable {
   IExecutable get(String script);

   Iterable<? extends IExecutable> scripts();
}
