package org.fuwjin.pogo.attr;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * An attribute which returns the matched input characters for a sub-expression.
 */
public class MatchAttribute implements Attribute {
   @Override
   public void prepare(final State state, final Map<String, Object> scope) {
      final Buffer buffer = state.buffer();
      scope.put("!match", buffer);
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      return state;
   }

   @Override
   public Type type() {
      return String.class;
   }

   @Override
   public String value(final State state, final Map<String, Object> scope) {
      final Buffer buffer = (Buffer)scope.get("!match");
      return buffer.release(state);
   }
}
