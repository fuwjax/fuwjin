package org.fuwjin.pogo;


public class Memo {
   private Object value;
   private final String name;
   private Position end;
   private final Position start;

   public Memo(final Position position, final String name, final Object value) {
      start = position;
      this.name = name;
      this.value = value;
   }

   public Position getEnd() {
      return end;
   }

   public Position getStart() {
      return start;
   }

   public Object getValue() {
      return value;
   }

   public String name() {
      return name;
   }

   public void setEnd(final Position position) {
      end = position;
   }

   public void setValue(final Object value) {
      this.value = value;
   }
}
