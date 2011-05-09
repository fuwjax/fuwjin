package org.fuwjin.chessur;

/**
 * A container for Scripts. The Catalog may also be used as a Script, usually as
 * a proxy for the first Script of the Catalog.
 */
public interface Catalog extends Script {
   /**
    * Returns a script by name. This method will always return a Script. If the
    * Script was not previously defined, then it will be created.
    * @param script the name of the script
    * @return the script, never null
    */
   Script get(String script);

   /**
    * Provides an iterator over the set of defined scripts.
    * @return the script iterator factory
    */
   Iterable<? extends Script> scripts();
}
