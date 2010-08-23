package org.fuwjin.pogo;

import org.fuwjin.postage.Adaptable;

public interface BufferedPosition extends Position {
   Position flush(Position last);

   Adaptable match(Position next);
}
