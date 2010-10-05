package org.fuwjin.postage;

/**
 * A factory for Function instances.
 */
public interface FunctionFactory {
   /**
    * Returns a function instance for the name. Returns null if no Function
    * could be found for the name.
    * @param name the function name.
    * @return the function, or null if no function exists
    */
   Function getFunction(String name);
}
