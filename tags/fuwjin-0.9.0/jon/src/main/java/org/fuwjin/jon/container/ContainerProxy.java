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
package org.fuwjin.jon.container;

import java.util.ArrayList;
import java.util.List;

public class ContainerProxy {
   public interface ResolveProxyTask {
      void resolve(final Object value);
   }

   private final List<ResolveProxyTask> tasks = new ArrayList<ResolveProxyTask>();

   public void addTask(final ResolveProxyTask task) {
      tasks.add(task);
   }

   public void resolve(final Object value) {
      for(final ResolveProxyTask task: tasks) {
         task.resolve(value);
      }
   }
}
