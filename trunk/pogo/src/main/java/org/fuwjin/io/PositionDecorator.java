package org.fuwjin.io;

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
   public void fail(final Position position) {
      this.position.fail(position);
   }

   @Override
   public void fail(final String reason, final Throwable cause) {
      position.fail(reason, cause);
   }

   @Override
   public PogoFailure failure() {
      return position.failure();
   }

   @Override
   public Object fetch(final String name) {
      return position.fetch(name);
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
   public boolean isAfter(final InternalPosition test) {
      return position.isAfter(test);
   }

   @Override
   public boolean isSuccess() {
      return position.isSuccess();
   }

   @Override
   public Object match(final Position next) {
      return AbstractInternalPosition.NO_MATCH;
   }

   @Override
   public void neutral() {
      position.neutral();
   }

   @Override
   public InternalPosition next() {
      return position.next();
   }

   @Override
   public Object release(final String name) {
      return position.release(name);
   }

   @Override
   public void reserve(final String name, final Object object) {
      position.reserve(name, object);
   }

   @Override
   public InternalPosition root() {
      return position.root();
   }

   @Override
   public void store(final String name, final Object object) {
      position.store(name, object);
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
