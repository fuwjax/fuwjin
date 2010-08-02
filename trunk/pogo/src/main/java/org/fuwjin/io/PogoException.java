package org.fuwjin.io;


public class PogoException extends Exception {
   private String name;
   private Position position;

   public PogoException(final String message) {
      super(message);
   }

   @Override
   public String getMessage() {
      return "Error parsing " + name + ": " + super.getMessage();
   }

   public PogoException label(final String name, final Position position) {
      this.position = position;
      this.name = name + "[" + position.line() + "," + position.column() + "]";
      return this;
   }

   public int position() {
      return position == null ? 0 : position.position();
   }
}
