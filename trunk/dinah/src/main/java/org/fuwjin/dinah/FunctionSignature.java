package org.fuwjin.dinah;

import java.lang.reflect.Type;

/**
 * Matches the signature of a function according to a set of constraints.
 */
public interface FunctionSignature {
   /**
    * Returns a similar signature that can accept the specified number of
    * arguments.
    * @param paramCount the required number of arguments
    * @return the new signature
    * @throws IllegalArgumentException if a signature cannot be produced
    */
   FunctionSignature accept(int paramCount);

   /**
    * Returns the category (usually the declaring type) for the signature.
    * @return the category
    */
   String category();

   /**
    * Returns true if this signature matches the set of fixed parameters.
    * @param params the set of parameters
    * @return true if the signature matches, false otherwise
    */
   boolean matchesFixed(Type... params);

   /**
    * Returns true if this signature matches the set of variable parameters. The
    * last parameter type should be an array.
    * @param params the set of parameters
    * @return true if the signature matches, false otherwise
    */
   boolean matchesVarArgs(Type... params);

   /**
    * Returns the full name of the signature.
    * @return the name
    */
   String name();
}
