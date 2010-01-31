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
package org.fuwjin.jon;

import org.fuwjin.io.ChildContext;
import org.fuwjin.io.ParseContext;
import org.fuwjin.io.PogoContext;
import org.fuwjin.io.RootContext;
import org.fuwjin.jon.container.JonContainer;

public class JonReadContext extends ParseContext {
   class JonChildContext extends ChildContext {
      private JonChildContext(final RootContext root) {
         super(root);
      }

      public Object addReference(final Reference ref) {
         container().store(ref.name(), ref.value());
         return ref.value();
      }

      public Object getReference(final String key) {
         return container().get(key);
      }

      public Reference newReference() {
         return new Reference(get());
      }
   }

   private final JonContainer container = new JonContainer();

   public JonReadContext(final CharSequence input) {
      super(input, null);
   }

   public JonContainer container() {
      return container;
   }

   @Override
   public PogoContext newChild() {
      return new JonChildContext(this);
   }
}
