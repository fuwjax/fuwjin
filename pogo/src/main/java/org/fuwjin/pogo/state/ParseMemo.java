package org.fuwjin.pogo.state;

public class ParseMemo {
   private String buffer;
   private Object value;
   private final AbstractState state;
   private final AbstractPosition start;
   private AbstractPosition end;
   private final String name;
   private int level;

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
      state.failStack(level, name, start);
   }

   public boolean isStored() {
      return end != null;
   }

   public String name() {
      return name;
   }

   public void release() {
      state.releaseMemo();
   }

   public void setLevel(final int level) {
      this.level = level;
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