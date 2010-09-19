package org.fuwjin.pogo.state;

/**
 * The memo structure for a pogo state.
 */
public class PogoMemo {
   private String buffer;
   private Object value;
   private final AbstractState state;
   private final AbstractPosition start;
   private AbstractPosition end;
   private final String name;
   private int level;

   /**
    * Creates a new instance.
    * @param name the rule name
    * @param state the state of the parse
    */
   public PogoMemo(final String name, final AbstractState state) {
      this.name = name;
      this.state = state;
      start = state.current();
   }

   /**
    * Returns the buffer.
    * @return the buffer
    */
   public String buffer() {
      return buffer;
   }

   /**
    * Returns the end position.
    * @return the end position
    */
   public AbstractPosition end() {
      return end;
   }

   /**
    * Adds an entry to the failure stack.
    */
   public void fail() {
      state.failStack(level, name, start);
   }

   /**
    * Returns true if this memo has been stored.
    * @return true if stored, false otherwise
    */
   public boolean isStored() {
      return end != null;
   }

   /**
    * Returns the name of the rule that generated the memo.
    * @return the rule name
    */
   public String name() {
      return name;
   }

   /**
    * Releases the memo.
    */
   public void release() {
      state.releaseMemo();
   }

   /**
    * Sets the memo stack level.
    * @param level the memo stack level
    */
   public void setLevel(final int level) {
      this.level = level;
   }

   /**
    * Stores the memo for further retrieval.
    * @param newBuffer the buffer
    * @param newValue the value
    */
   public void store(final String newBuffer, final Object newValue) {
      buffer = newBuffer;
      value = newValue;
      end = state.current();
      start.setMemo(this);
   }

   /**
    * Returns the stored value.
    * @return the stored value
    */
   public Object value() {
      return value;
   }
}