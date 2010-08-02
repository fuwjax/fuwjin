/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import org.fuwjin.io.ChildContext;
import org.fuwjin.io.ParseContext;
import org.fuwjin.io.PogoContext;
import org.fuwjin.io.RootContext;
import org.fuwjin.jon.container.JonContainer;

public class JonReadContext extends ParseContext {
   class JonChildContext extends ChildContext {
      private JonChildContext(final String name, final RootContext root) {
         super(name, root);
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
      super(input);
   }

   public JonContainer container() {
      return container;
   }

   @Override
   public PogoContext newChild(final String name) {
      return new JonChildContext(name, this);
   }
}
