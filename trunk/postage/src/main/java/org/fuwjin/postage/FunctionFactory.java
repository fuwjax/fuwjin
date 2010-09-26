package org.fuwjin.postage;

import java.lang.reflect.Type;

/**
 * A factory for Function instances.
 */
public interface FunctionFactory {
   /**
    * Returns a function instance for the name and parameter list.
    * @param name the function name.
    * @param parameters the type parameters for a returned function
    * @return the function
    */
   Function getFunction(String name, Type... parameters);

   /**
    * Returns the prefix of Functions produced by this factory.
    * @return the factory prefix
    */
   String name();
}
