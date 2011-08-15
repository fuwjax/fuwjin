package org.fuwjin.chessur;

/**
 * Abstraction over a collection of scripts.
 */
@Deprecated
public interface Module {
   /**
    * Returns a script by name. This method will always return a Script. If the
    * Script was not previously defined, then it will be created.
    * @param script the name of the script
    * @return the script, never null
    */
   Script get(String script);

   /**
    * Returns the module name.
    * @return the name
    */
   String name();

   /**
    * Provides an iterator over the set of defined scripts.
    * @return the script iterator factory
    */
   Iterable<? extends Script> scripts();

   /**
    * Returns the module source, usually the path to the catalog file.
    * @return the source
    */
   String source();
}
