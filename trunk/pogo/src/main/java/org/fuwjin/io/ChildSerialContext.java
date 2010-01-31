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
package org.fuwjin.io;

import static java.lang.Character.isHighSurrogate;
import static java.lang.Character.toCodePoint;

/**
 * A child PogoContext that maps the buffered object to its toString during an
 * append call.
 */
public class ChildSerialContext extends ChildContext {
   /**
    * Creates a new instance.
    * @param root the root context
    */
   public ChildSerialContext(final RootContext root) {
      super(root);
   }

   @Override
   public void accept() {
      final String str = get().toString();
      for(int i = 0; i < str.length(); i++) {
         final char c1 = str.charAt(i);
         if(isHighSurrogate(c1)) {
            final char c2 = str.charAt(++i);
            accept(toCodePoint(c1, c2));
         } else {
            accept(c1);
         }
      }
      success(true, null);
   }
}