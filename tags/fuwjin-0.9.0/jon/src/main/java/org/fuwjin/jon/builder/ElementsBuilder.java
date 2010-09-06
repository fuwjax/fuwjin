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
package org.fuwjin.jon.builder;

import java.util.List;

import org.fuwjin.jon.container.ContainerProxy;
import org.fuwjin.jon.container.ContainerProxy.ResolveProxyTask;

public abstract class ElementsBuilder extends Builder {
   protected List<Object> list;

   public ElementsBuilder(final Class<?> type) {
      super(type);
      list = (List<Object>)newInstance();
   }

   public void add(final Object element) {
      if(element instanceof ContainerProxy) {
         final int index = addPlaceholder();
         ((ContainerProxy)element).addTask(new ResolveProxyTask() {
            public void resolve(final Object value) {
               postAdd(index, value);
            }
         });
      } else {
         addImpl(element);
      }
   }

   public abstract Builder newElement();

   protected void addImpl(final Object value) {
      list.add(value);
   }

   protected int addPlaceholder() {
      final int index = list.size();
      list.add(null);
      return index;
   }

   protected List<?> list() {
      return list;
   }

   protected abstract void postAdd(int index, Object value);
}
