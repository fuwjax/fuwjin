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
package org.fuwjin.postage;

import org.fuwjin.postage.function.CurriedTarget;

public class CurryTransform implements TargetTransform {
   private final Object[] args;

   public CurryTransform(final Object[] args) {
      this.args = args;
   }

   @Override
   public FunctionTarget transform(final FunctionTarget target) {
      return CurriedTarget.curry(target, args);
   }
}
