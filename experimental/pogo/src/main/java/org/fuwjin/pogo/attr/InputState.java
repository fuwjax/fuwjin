package org.fuwjin.pogo.attr;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.pogo.CodePointStream;

public class InputState implements State {
   private final int ch;
   private final CodePointStream input;
   private State next;

   public InputState(final CodePointStream input) {
      this.input = input;
      ch = input.next();
   }

   @Override
   public Buffer buffer() {
      return null;
   }

   @Override
   public int codePoint() {
      return ch;
   }

   @Override
   public State failure(final Object cause, final String message, final Object... args) {
      return new FailureState(cause, message, args);
   }

   @Override
   public State failure(final String message, final Object... args) {
      return failure(null, message, args);
   }

   @Override
   public boolean isSuccess() {
      return true;
   }

   @Override
   public Map<String, Object> newScope(final Map<String, Object> scope) {
      final Map<String, Object> map = new HashMap<String, Object>();
      map.putAll(scope);
      return map;
   }

   @Override
   public State next() {
      if(next == null) {
         if(ch == -1) {
            return failure("There is no next following EOF");
         }
         next = new InputState(input);
      }
      return next;
   }
}
