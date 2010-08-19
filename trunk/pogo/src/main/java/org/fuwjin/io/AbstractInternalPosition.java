package org.fuwjin.io;

public abstract class AbstractInternalPosition implements InternalPosition, BufferedPosition {
   public static final Object NO_MATCH = new Object() {
      @Override
      public String toString() {
         throw new UnsupportedOperationException();
      }
   };
   private int line;
   private int column;
   private final PogoFailure failure = new PogoFailure(this);
   private final ParseStack stack;

   protected AbstractInternalPosition() {
      line = 1;
      column = 0;
      stack = new ParseStack();
   }

   protected AbstractInternalPosition(final AbstractInternalPosition prev) {
      stack = prev.stack;
      line = prev.line;
      column = prev.column;
   }

   protected void accept(final int ch) {
      if(ch == '\n') {
         line++;
         column = 1;
      } else if(ch != -1) {
         column++;
      }
   }

   protected void accept(final String str) {
      if(str != null) {
         column += str.length();
         int i = str.indexOf('\n', -1);
         while(i != -1) {
            line++;
            column = str.length() - i;
            i = str.indexOf('\n', i);
         }
      }
   }

   @Override
   public InternalPosition advance(final int low, final int high) {
      check(low, high);
      if(!isSuccess()) {
         return this;
      }
      return next();
   }

   @Override
   public void assertSuccess() throws PogoException {
      if(!failure.isSuccess()) {
         throw failure.exception();
      }
   }

   protected void fail(final int low, final int high) {
      failure.addFailure(low, high);
   }

   @Override
   public void fail(final Position child) {
      failure.addFailure(((InternalPosition)child).failure());
   }

   @Override
   public void fail(final String reason, final Throwable cause) {
      failure.addFailure(reason, cause);
   }

   @Override
   public PogoFailure failure() {
      return failure;
   }

   @Override
   public Object fetch(final String name) {
      return stack.lookup(name);
   }

   @Override
   public Position flush(final Position last) {
      return last;
   }

   @Override
   public InternalPosition internal() {
      return this;
   }

   @Override
   public boolean isAfter(final InternalPosition position) {
      final AbstractInternalPosition ip = (AbstractInternalPosition)position.root();
      return line > ip.line || line == ip.line && column > ip.column;
   }

   @Override
   public boolean isSuccess() {
      return failure.isSuccess();
   }

   @Override
   public Object match(final Position next) {
      return NO_MATCH;
   }

   @Override
   public void neutral() {
      failure.neutral();
   }

   @Override
   public Object release(final String name) {
      return stack.release(name, isSuccess());
   }

   @Override
   public void reserve(final String name, final Object state) {
      stack.reserve(name, state);
   }

   @Override
   public InternalPosition root() {
      return this;
   }

   @Override
   public void store(final String name, final Object state) {
      stack.store(name, state);
   }

   @Override
   public void success() {
      failure.clear();
   }

   @Override
   public String toMessage() {
      return stack.toMessage() + "[" + line + "," + column + "]";
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      if(failure.isSuccess()) {
         builder.append('+');
      } else {
         builder.append('-');
      }
      builder.append('[').append(line).append(',').append(column).append("] \"");
      append(builder);
      return builder.append('"').toString();
   }

   @Override
   public BufferedPosition unbuffered() {
      return this;
   }
}
