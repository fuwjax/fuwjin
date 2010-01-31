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
