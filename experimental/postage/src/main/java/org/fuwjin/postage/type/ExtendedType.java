/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.postage.type;

import java.lang.reflect.Type;

public interface ExtendedType extends Type {
   String getCanonicalName();

   Type getComponentType();

   Type[] getUpperBounds();

   boolean isAssignableFrom(Class<?> test);

   boolean isAssignableTo(Type test);

   boolean isClass();

   boolean isInstance(Object object);
}
