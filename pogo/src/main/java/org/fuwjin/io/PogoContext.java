/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.io;

/**
 * The specification for a parse context during a Pogo transformation.
 */
public interface PogoContext {
   /**
    * Transforms the next code point.
    */
   void accept();

   /**
    * Transforms the next code point if it equals {@code ch}.
    * @param ch the expected code point
    */
   void accept(int ch);

   /**
    * Transforms the next code point if it is between {@code start} and
    * {@code end} inclusive.
    * @param start the smallest allowed code point
    * @param end the largest allowed code point
    */
   void accept(int start, int end);

   PogoException failedCheck(String message);

   /**
    * Returns the context object. May be null if the transform produced a null
    * or if the object has not yet been set.
    * @return the context object
    */
   Object get();

   /**
    * Returns true if the current context indicates a successful transformation.
    * @return true if successful, false otherwise
    */
   boolean isSuccess();

   /**
    * The raw match from a successful transformation.
    * @return the raw match
    */
   String match();

   /**
    * Creates a new child context.
    * @param newObject the child's context object
    * @param newSuccess the child's initial success state
    * @param failureReason the reason for the failure if the initial success
    *        state is false
    * @return the new child context
    */
   PogoContext newChild(String name, Object newObject, PogoException failure);

   /**
    * Returns the current position. Used primarily for seeking, the position
    * should generally be increasing as successful accept calls are made.
    * However, there is no guarantee on the difference between successive values
    * of position and therefore mathematical operations with its result are
    * discouraged.
    * @return the current position.
    */
   Position position();

   PogoException postageException(Object obj);

   /**
    * Resets the context to an earlier position. The mark should come from an
    * unmodified result of a previous call to position().
    * @param mark the new position
    */
   void seek(Position mark);

   /**
    * Updates the current context object.
    * @param newObject the new object
    * @param newSuccess the current success state
    * @param failureReason the reason for the failure if success is false
    * @return this context
    */
   PogoContext set(Object newObject, PogoException failure);

   /**
    * Updates the current success state of the context.
    * @param state the new success state
    * @param failureReason the reason for the failure if state is false
    * @return the new success state
    */
   void success(PogoException failure);
}
