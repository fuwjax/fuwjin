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

/**
 * Builds a list.
 */
public abstract class ElementsBuilder extends Builder {
   protected List<Object> list;

   /**
    * Creates a new instance.
    * @param type the type
    */
   public ElementsBuilder(final Class<?> type) {
      super(type);
      list = (List<Object>)newInstance();
   }

   /**
    * Adds a new element.
    * @param element the new element
    */
   public void add(final Object element) {
      if(element instanceof ContainerProxy) {
         final int index = addPlaceholder();
         ((ContainerProxy)element).addTask(new ResolveProxyTask() {
            @Override
            public void resolve(final Object value) {
               postAdd(index, value);
            }
         });
      } else {
         addImpl(element);
      }
   }

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

   /**
    * Returns a builder for the elements.
    * @return the builder
    */
   public abstract Builder newElement();

   protected abstract void postAdd(int index, Object value);
}
