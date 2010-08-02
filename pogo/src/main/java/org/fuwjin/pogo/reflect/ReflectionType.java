/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo.reflect;

import org.fuwjin.postage.Function;

/**
 * An abstraction of Class that produces message dispatch objects.
 */
public interface ReflectionType {
   /**
    * Creates a new dispatcher.
    * @param name the name to dispatch
    * @return the dispatcher for the name
    */
   public Function getInvoker(String name);

   /**
    * Returns true if {@code input} is an instance of this type.
    * @param input the input to test
    * @return true if input is an instance of this type, false otherwise
    */
   public boolean isInstance(final Object input);
}
