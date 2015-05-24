package org.fuwjin.pogo.attr;

import java.text.ParseException;
import java.util.Map;

public class FailureState implements State {
   public FailureState(final Object cause, final String message, final Object[] args) {
      // TODO Auto-generated constructor stub
   }

   @Override
   public Buffer buffer() {
      throw new UnsupportedOperationException();
   }

   @Override
   public int codePoint() {
      throw new UnsupportedOperationException();
   }

   public Exception exception() {
      return new ParseException("oops", 1);
   }

   @Override
   public State failure(final Object cause, final String message, final Object... args) {
      throw new UnsupportedOperationException();
   }

   @Override
   public State failure(final String message, final Object... args) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isSuccess() {
      return false;
   }

   @Override
   public Map<String, Object> newScope(final Map<String, Object> scope) {
      throw new UnsupportedOperationException();
   }

   @Override
   public State next() {
      throw new UnsupportedOperationException();
   }
}
