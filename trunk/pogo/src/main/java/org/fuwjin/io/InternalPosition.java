package org.fuwjin.io;

public interface InternalPosition extends Position {
   void append(Appendable appender);

   void check(int low, int high);

   PogoFailure failure();

   InternalPosition internal();

   boolean isAfter(InternalPosition position);

   InternalPosition next();

   InternalPosition root();

   String toMessage();
}
