/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

/**
 * A task run before a rule is started.
 */
public interface ConstructTask {
   /**
    * Creates a child context.
    * @param input the parent context
    * @return the child context
    */
   Object initialize(Object input);

   /**
    * Sets the message dispatch type.
    * @param type the type to source for dispatch
    */
   void setType(ReflectionType type);
}
