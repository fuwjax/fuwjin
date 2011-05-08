package org.fuwjin.chessur.stream;

public interface Position {
   int column();

   int index();

   boolean isValid();

   int line();

   Object value();

   String valueString();
}
