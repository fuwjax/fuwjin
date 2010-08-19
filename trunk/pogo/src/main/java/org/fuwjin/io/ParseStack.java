package org.fuwjin.io;

public class ParseStack {
   private static class ParseState {
      private final String name;
      private Object obj;
      private final ParseState prev;

      public ParseState(final String name, final Object obj, final ParseState prev) {
         this.name = name;
         this.obj = obj;
         this.prev = prev;
      }
   }

   private ParseState current;

   public ParseStack() {
      this(null);
   }

   public ParseStack(final Object root) {
      reserve(null, root);
   }

   public Object current() {
      return current.obj;
   }

   public Object lookup(final String name) {
      ParseState s = current;
      while(s != null) {
         if(name == null ? s.name == null : name.equals(s.name)) {
            return s.obj;
         }
         s = s.prev;
      }
      return null;
   }

   public Object release(final String name, final boolean isComplete) {
      assert name.equals(current.name);
      final Object ret = current.obj;
      current = current.prev;
      return ret;
   }

   public void reserve(final String name, final Object state) {
      current = new ParseState(name, state, current);
   }

   public void store(final String name, final Object state) {
      assert name.equals(current.name): name + " vs " + current.name;
      current.obj = state;
   }

   public String toMessage() {
      return current.name;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      ParseState s = current;
      while(s != null) {
         builder.append(s.name).append(": ").append(s.obj == null ? "null" : s.obj.getClass()).append('\n');
         s = s.prev;
      }
      return builder.toString();
   }
}
