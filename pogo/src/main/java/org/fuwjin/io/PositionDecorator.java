package org.fuwjin.io;

import org.fuwjin.pogo.BufferedPosition;
import org.fuwjin.pogo.Memo;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.Position;
import org.fuwjin.postage.Adaptable;
import org.fuwjin.postage.StandardAdaptable;

public abstract class PositionDecorator implements BufferedPosition, InternalPosition {
   private final InternalPosition position;

   public PositionDecorator(final InternalPosition position) {
      this.position = position;
   }

   @Override
   public Position advance(final int low, final int high) {
      return position.advance(low, high);
   }

   @Override
   public void append(final Appendable appender) {
      position.append(appender);
   }

   @Override
   public void assertSuccess() throws PogoException {
      position.assertSuccess();
   }

   @Override
   public void check(final int low, final int high) {
      position.check(low, high);
   }

   @Override
   public Memo createMemo(final String name, final Object value) {
      return position.createMemo(name, value);
   }

   @Override
   public void fail(final String reason, final Throwable cause) {
      position.fail(reason, cause);
   }

   @Override
   public Position flush(final Position last) {
      return last;
   }

   @Override
   public InternalPosition internal() {
      return position;
   }

   @Override
   public boolean isAfter(final Position test) {
      return position.isAfter(test);
   }

   @Override
   public boolean isSuccess() {
      return position.isSuccess();
   }

   @Override
   public Adaptable match(final Position next) {
      return StandardAdaptable.UNSET;
   }

   @Override
   public Memo memo() {
      return position.memo();
   }

   @Override
   public InternalPosition next() {
      return position.next();
   }

   @Override
   public void record(final Memo memo) {
      position.record(memo);
   }

   @Override
   public Memo releaseMemo(final Memo newMemo) {
      return position.releaseMemo(newMemo);
   }

   @Override
   public InternalPosition root() {
      return position.root();
   }

   @Override
   public void success() {
      position.success();
   }

   @Override
   public String toMessage() {
      return position.toMessage();
   }

   @Override
   public String toString() {
      return position.toString();
   }
}
