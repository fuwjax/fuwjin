package org.fuwjin.sample;

/**
 * A sample interface.
 */
public interface VarArgsInterface {
   /**
    * A sample sub-interface.
    */
   interface VarArgsChild extends VarArgsInterface {
      // adds nothing
   }

   /**
    * The interface method.
    * @param values some values
    */
   void setValues(final String... values);
}
