/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.lifeguard;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Monitors the state of a pooled object.
 * @param <T> the pooled object type
 */
public abstract class PooledResource<T>{
   /**
    * Returns an already closed pooled object.
    * @param <T> the type for the null object
    * @return the new closed object
    */
   public static <T>PooledResource<T> newClosedResource(){
      return new PooledResource<T>(State.CLOSED){
         @Override
         protected void close(){
            throw new UnsupportedOperationException();
         }

         @Override
         protected T get(){
            throw new UnsupportedOperationException();
         }
      };
   }

   private final AtomicReference<State> state;

   /**
    * Creates a new instance in the ready state.
    */
   protected PooledResource(){
      this(State.READY);
   }

   /**
    * Creates a new instance.
    * @param start the initial state
    */
   private PooledResource(final State start){
      state = new AtomicReference<State>(start);
   }

   /**
    * Closes the underlying connection.
    */
   final void closeIfNotClosed(){
      if(changeState(State.READY, State.CLOSED) || changeState(State.ACTIVE, State.CLOSED)){
         close();
      }
   }

   /**
    * Returns the underlying connection if the state can transition from Ready
    * to Active. Otherwise returns null.
    * 
    * This method should not be called directly outside of LifeGuard. 
    * Instead use LifeGuard.get().
    * @return the connection if the current state is Ready, null otherwise.
    * @throws Exception if there is a problem
    */
   final T getIfReady() throws Exception{
      if(!changeState(State.READY, State.ACTIVE)){
         return null;
      }
      try{
         return get();
      }catch(final Exception e){
         abandon();
         throw e;
      }
   }

   /**
    * Returns true if the connection is closed.
    * @return true if the connection is closed, false otherwise
    */
   public final boolean isClosed(){
      return is(State.CLOSED);
   }

   /**
    * Abandons an active object. This method will move to the closed state, and
    * therefore close the underlying pooled object.
    */
   protected final void abandon(){
      if(changeState(State.ACTIVE, State.CLOSED)){
         close();
      }
   }

   /**
    * Closes the underlying connection if in the Ready state.
    * 
    * This method should not be called directly outside of PooledResource. 
    * Instead use abandon() or release().
    */
   final void closeIfReady(){
      if(changeState(State.READY, State.CLOSED)){
         close();
      }
   }

   /**
    * Closes the underlying pooled object. This method should only throw a
    * runtime exception if the underlying pooled object is still in a
    * valid state after this method completes. 
    * 
    * This method should not be called directly outside of PooledResource. 
    * Instead use abandon() or release().
    */
   protected abstract void close();

   /**
    * Returns the pooled object. This method should not be called directly 
    * except from PooledResource. Instead use LifeGuard.get().
    * @return the pooled object
    * @throws Exception if the pooled object cannot be returned in a valid state
    */
   protected abstract T get() throws Exception;

   /**
    * Returns an active object to the pool.
    */
   protected final void release(){
      changeState(State.ACTIVE, State.READY);
   }

   /**
    * Changes the internal state from {@code from} to {@code to}. A successful
    * transition to Closed will close the underlying connection.
    * @param from the expected current state
    * @param to the new state
    * @return true if the state was transitioned, false otherwise
    */
   private boolean changeState(final State from, final State to){
      assert from != State.CLOSED;
      return state.compareAndSet(from, to);
   }

   /**
    * Tests for the {@code expected} state.
    * @param expected the expected state
    * @return true if currently in the expected state, false otherwise
    */
   private boolean is(final State expected){
      return expected == state.get();
   }

   /**
    * Pooled object states.
    */
   private enum State{
      ACTIVE, CLOSED, READY
   }
}
