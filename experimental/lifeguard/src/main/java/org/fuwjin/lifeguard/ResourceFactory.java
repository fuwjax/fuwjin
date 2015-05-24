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
package org.fuwjin.lifeguard;

/**
 * Creates new instances of pooled objects.
 * @param <T> the pooled object types
 */
public interface ResourceFactory<T extends Resource> {
   /**
    * Creates a new pooled object instance.
    * @return the new pooled object instance
    * @throws Exception if there is any problem creating the new instance
    */
   T newResource() throws Exception;
}
