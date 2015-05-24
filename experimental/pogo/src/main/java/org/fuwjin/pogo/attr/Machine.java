package org.fuwjin.pogo.attr;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.pogo.CodePointStream;

public abstract class Machine {
   protected abstract Expression expression();

   public Object transform(final CodePointStream stream) throws Exception {
      final Map<String, Object> scope = new HashMap<String, Object>();
      final State fromState = new InputState(stream);
      final State toState = expression().transition(fromState, scope);
      if(!toState.isSuccess()) {
         throw ((FailureState)toState).exception();
      }
      return scope.get("this");
   }
}
