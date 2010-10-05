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
package org.fuwjin.jon.container;

import java.util.ArrayList;
import java.util.List;

/**
 * A container that reserves new deserialization tasks until the target object
 * can be created. Effectively this class resolves any forward references in
 * JON.
 */
public class ContainerProxy {
   /**
    * Performs a deserialization task for a future target.
    */
   public interface ResolveProxyTask {
      /**
       * Resolves the deserialization task.
       * @param value the target object
       */
      void resolve(final Object value);
   }

   private final List<ResolveProxyTask> tasks = new ArrayList<ResolveProxyTask>();

   /**
    * Adds a new deserialization task.
    * @param task the task
    */
   public void addTask(final ResolveProxyTask task) {
      tasks.add(task);
   }

   /**
    * Resolves the set of deserialization tasks.
    * @param value the target.
    */
   public void resolve(final Object value) {
      for(final ResolveProxyTask task: tasks) {
         task.resolve(value);
      }
   }
}
