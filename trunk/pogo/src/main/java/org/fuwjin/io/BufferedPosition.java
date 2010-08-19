package org.fuwjin.io;

public interface BufferedPosition extends Position {
   Position flush(Position last);

   Object match(Position next);
}
