package org.fuwjin.lifeguard;


public interface Resource<T>{
   /**
    * Closes the underlying object. This method should only throw an exception
    * if the underlying object remains in an open 
    */
   void close();

   /**
    * Returns the pooled object. This method should not be called directly 
    * except from PooledResource. Instead use LifeGuard.get().
    * @return the pooled object
    * @throws Exception if the pooled object cannot be returned in a valid state
    */
   T get() throws Exception;
}