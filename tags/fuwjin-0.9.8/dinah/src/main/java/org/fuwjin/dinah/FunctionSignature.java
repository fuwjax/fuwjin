package org.fuwjin.dinah;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter.AdaptException;

/**
 * The real signature of a Function. Every function will have a single
 * signature, however, several functions may share the same signature.
 */
public interface FunctionSignature {
   Object[] adapt(Object[] args) throws AdaptException;

   Type argType(int index);

   boolean canAdapt(Type[] types);

   /**
    * Returns the category (usually the declaring type) for the signature.
    * @return the category
    */
   Type category();

   FunctionSignature meets(SignatureConstraint constraint);

   String memberName();

   /**
    * Returns the full name of the signature.
    * @return the name
    */
   String name();

   boolean supportsArgs(int count);
}
