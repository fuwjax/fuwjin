package org.fuwjin.grin.env;

import org.fuwjin.chessur.expression.AbortedException;
import org.fuwjin.chessur.expression.Expression;
import org.fuwjin.chessur.expression.ResolveException;

/**
 * Grin logging and exception management.
 */
public interface Trace {
   /**
    * Aborts the entire resolve stack.
    * @param pattern the message pattern
    * @param args the message arguments
    * @return the aborted exception
    */
   AbortedException abort(String pattern, Object... args);

   /**
    * Aborts the entire resolve stack.
    * @param cause the exception cause
    * @param pattern the message pattern
    * @param args the message arguments
    * @return the aborted exception
    */
   AbortedException abort(Throwable cause, String pattern, Object... args);

   void accept() throws ResolveException;

   /**
    * Fails the current resolve operation.
    * @param pattern the message pattern
    * @param args the message arguments
    * @return the resolve expression
    */
   ResolveException fail(String pattern, Object... args);

   /**
    * Fails the current resolve operation.
    * @param cause the exception cause
    * @param pattern the message pattern
    * @param args the message arguments
    * @return the resolve expression
    */
   ResolveException fail(Throwable cause, String pattern, Object... args);

   Object get(String name);

   void log(Object value) throws AbortedException;

   Trace newInput(String in);

   Trace newOutput();

   int next() throws ResolveException;

   void publish(Object value) throws AbortedException;

   void put(String name, Object value);

   /**
    * Resolves the expression.
    * @param value the expression to resolve
    * @return the result of the resolve
    * @throws AbortedException if the resolve is aborted
    * @throws ResolveException if the resolve fails
    */
   Object resolve(Expression value) throws AbortedException, ResolveException;

   Object resolve(String name, Expression expression) throws AbortedException, ResolveException;

   /**
    * Resolves the expression without changing the input, output, or scope.
    * @param value the expression to resolve
    * @return the result of the resolve
    * @throws AbortedException if the resolve is aborted
    * @throws ResolveException if the resolve fails
    */
   Object resolveAndRevert(Expression value) throws AbortedException, ResolveException;

   Object resolveMatch(String name, Expression expression) throws AbortedException, ResolveException;
}
