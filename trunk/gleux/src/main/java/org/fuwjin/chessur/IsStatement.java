package org.fuwjin.chessur;

import java.lang.reflect.Array;

/**
 * Allows testing for default values.
 */
public class IsStatement implements Expression {
   private static boolean isDefault(final Object value) {
      if(value == null) {
         return true;
      }
      if(value instanceof Boolean) {
         return !(Boolean)value;
      }
      if(value instanceof Number) {
         return ((Number)value).doubleValue() == 0;
      }
      if(value instanceof String) {
         return ((String)value).length() == 0;
      }
      if(value.getClass().isArray()) {
         return Array.getLength(value) == 0;
      }
      return false;
   }

   private final boolean isNot;
   private final Expression value;

   /**
    * Creates a new instance.
    * @param isNot true if the failure should be reversed
    * @param value the value to test for default equality
    */
   public IsStatement(final boolean isNot, final Expression value) {
      this.isNot = isNot;
      this.value = value;
   }

   @Override
   public String toString() {
      return "is " + (isNot ? "not " : "") + value;
   }

   @Override
   public State transform(final State state) {
      final State result = value.transform(state);
      if(!result.isSuccess()) {
         return isNot ? state : result;
      }
      if(isDefault(result.value()) ^ isNot) {
         return state.failure(result.failure("unexpected value"), "failed is test");
      }
      return state;
   }
}
