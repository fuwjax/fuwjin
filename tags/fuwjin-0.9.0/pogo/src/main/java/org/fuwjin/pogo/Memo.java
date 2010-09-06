package org.fuwjin.pogo;

/**
 * The state object for memoization.
 */
public class Memo {
   private Object value;
   private final String name;
   private Position end;
   private final Position start;

   /**
    * Creates a new instance.
    * @param position the start position
    * @param name the name of the rule being memoized
    * @param value the value of the parse
    */
   public Memo(final Position position, final String name, final Object value) {
      start = position;
      this.name = name;
      this.value = value;
   }

   /**
    * Returns the end position.
    * @return the end position
    */
   public Position getEnd() {
      return end;
   }

   /**
    * Returns the start position.
    * @return the start position
    */
   public Position getStart() {
      return start;
   }

   /**
    * Returns the memoized value.
    * @return the value
    */
   public Object getValue() {
      return value;
   }

   /**
    * Returns the rule name.
    * @return the name
    */
   public String name() {
      return name;
   }

   /**
    * Sets the end position.
    * @param position the end position
    */
   public void setEnd(final Position position) {
      end = position;
   }

   /**
    * Updates the memoized value
    * @param value the new value
    */
   public void setValue(final Object value) {
      this.value = value;
   }
}
