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

import java.lang.reflect.AccessibleObject;

public class ObjectUtils {
   public static <T extends AccessibleObject> T access(final T obj) {
      if(!obj.isAccessible()) {
         obj.setAccessible(true);
      }
      return obj;
   }
}
