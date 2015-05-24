/*
 * This file is part of The Fuwjin Suite.
 *
 * The Fuwjin Suite is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Fuwjin Suite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2008 Michael Doberenz
 */
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