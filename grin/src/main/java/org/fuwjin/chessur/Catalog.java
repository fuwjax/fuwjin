package org.fuwjin.chessur;

/**
 * A container for Scripts. The Catalog may also be used as a Script, usually as
 * a proxy for the first Script of the Catalog.
 */
@Deprecated
public interface Catalog extends Script, Module {
   // a catalog is a module that can be treated like a script
}
