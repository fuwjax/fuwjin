package org.fuwjin.pogo.state;

import org.fuwjin.postage.Failure;

public interface PogoState {
   boolean advance(final int start, final int end);

   PogoPosition buffer(final boolean required);

   PogoPosition current();

   ParseException exception();

   void fail(final String string, final Failure cause);

   ParseMemo getMemo(final String name, final boolean needsBuffer);

   Object getValue();

   boolean isAfter(final PogoPosition mark);

   PogoPosition mark();

   void setValue(final Object object);
}