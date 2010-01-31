/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

import org.fuwjin.io.PogoContext;

/**
 * A task run after a rule has completed.
 */
public interface FinalizerTask {
   /**
    * Migrates the child context object to the container context object.
    * @param container the parent context
    * @param child the child context
    */
   void finalize(PogoContext container, PogoContext child);

   /**
    * Sets the message dispatch type.
    * @param type the type to source for dispatch
    */
   void setType(ReflectionType type);
}
