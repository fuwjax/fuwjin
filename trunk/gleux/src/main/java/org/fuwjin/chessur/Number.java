package org.fuwjin.chessur;

public class Number extends java.lang.Number implements Expression {
   private final String value;

   public Number(final String value) {
      this.value = value;
   }

   @Override
   public double doubleValue() {
      return Double.parseDouble(value);
   }

   @Override
   public float floatValue() {
      return Float.parseFloat(value);
   }

   @Override
   public int intValue() {
      return Integer.parseInt(value);
   }

   @Override
   public long longValue() {
      return Long.parseLong(value);
   }

   @Override
   public String toString() {
      return value;
   }

   @Override
   public State transform(final State state) {
      return state.value(this);
   }
}
