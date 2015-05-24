package org.fuwjin.pogo.attr;

import java.util.Map;

/**
 * Manages the current state of an Expression.
 */
public interface State {
   /**
    * Returns an input buffer.
    * @return the new buffer
    */
   Buffer buffer();

   /**
    * Returns the current character.
    * @return the current character
    */
   int codePoint();

   /**
    * Returns a failure state.
    * @param cause the cause of the failure
    * @param message the failure message pattern
    * @param args the message arguments
    * @return the failure state
    */
   State failure(Object cause, String message, Object... args);

   /**
    * Returns a failure state.
    * @param message the failure message pattern
    * @param args the message arguments
    * @return the failure state
    */
   State failure(String message, Object... args);

   /**
    * Returns true if this state is a successful state.
    * @return true if the state is a success, false if it is a failure state
    */
   boolean isSuccess();

   /**
    * Creates a new scope.
    * @param scope the current scope
    * @return the new scope
    */
   Map<String, Object> newScope(Map<String, Object> scope);

   /**
    * Returns the next state.
    * @return the next state
    */
   State next();
}
