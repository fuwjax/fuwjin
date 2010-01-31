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
package org.fuwjin.test.object;

/**
 * A test case that builds a replacement for one of its parent's fields
 * @author michaeldoberenz
 */
public class ComplexChild extends SimpleObject {
   /**
    * Creates a new instance.
    */
   public ComplexChild() {
      super("wow", new IntegerChild(new String[]{"bob", "was", "here"}, 19)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
   }
}
