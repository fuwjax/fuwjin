package org.fuwjin.pogo.state;

public class ParseMemo {
   private String buffer;
   private Object value;
   private final AbstractState state;
   private final AbstractPosition start;
   private AbstractPosition end;
   private final String name;

   public ParseMemo(final String name, final AbstractState state) {
      this.name = name;
      this.state = state;
      start = state.current();
   }

   public String buffer() {
      return buffer;
   }

   public AbstractPosition end() {
      return end;
   }

   public void fail() {
      state.failStack(name, start);
   }

   public boolean isStored() {
      return end != null;
   }

   public String name() {
      return name;
   }

   public void store(final String buffer, final Object value) {
      this.buffer = buffer;
      this.value = value;
      end = state.current();
      start.setMemo(this);
   }

   public Object value() {
      return value;
   }
}