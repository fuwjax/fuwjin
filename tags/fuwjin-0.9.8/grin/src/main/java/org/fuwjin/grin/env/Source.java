package org.fuwjin.grin.env;

import org.fuwjin.chessur.expression.ResolveException;

/**
 * Grin input stream.
 */
public interface Source {
   int EOF = -1;

   int next() throws ResolveException;

   void read() throws ResolveException;
}
