package org.fuwjin.io;

import org.fuwjin.pogo.Memo;
import org.fuwjin.pogo.Position;

public interface InternalPosition extends Position {
   void append(Appendable appender);

   void check(int low, int high);

   InternalPosition internal();

   InternalPosition next();

   void record(Memo memo);

   InternalPosition root();

   String toMessage();
}
