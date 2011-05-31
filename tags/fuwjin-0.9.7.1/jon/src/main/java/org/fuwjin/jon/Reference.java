package org.fuwjin.jon;

import java.util.ArrayList;
import java.util.List;

public class Reference {
   private final List<Task> tasks = new ArrayList<Task>();
   private Object value;
   private boolean resolved;

   public void addTask(final Task task) throws Exception {
      if(resolved) {
         task.resolve(value);
      } else {
         tasks.add(task);
      }
   }

   public void resolve(final Object value) throws Exception {
      resolved = true;
      this.value = value;
      for(final Task task: tasks) {
         task.resolve(value);
      }
   }

   public Object value() {
      if(!resolved) {
         throw new IllegalStateException("reference has not resolved yet");
      }
      return value;
   }
}
