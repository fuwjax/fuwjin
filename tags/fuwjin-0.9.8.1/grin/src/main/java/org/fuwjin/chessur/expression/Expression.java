/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur.expression;

import org.fuwjin.grin.env.Trace;

/**
 * Resolves an input stream and scope to an output stream and return value.
 */
public interface Expression {
   /**
    * Resolves an input stream and scope to an output stream and return value.
    * @return the resolved value
    * @throws AbortedException if the script intentionally aborts
    * @throws ResolveException if the script fails
    */
   Object resolve(Trace trace) throws AbortedException, ResolveException;
}
