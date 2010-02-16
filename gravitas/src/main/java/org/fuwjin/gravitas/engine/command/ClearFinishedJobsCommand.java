package org.fuwjin.gravitas.engine.command;

import static org.fuwjin.gravitas.engine.ExecutionEngine.executions;

import java.util.Iterator;

import org.fuwjin.gravitas.engine.Execution;
import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;

public class ClearFinishedJobsCommand implements Runnable{
   @Inject
   private Context source;
   
   @Override
   public void run(){
      int removed = 0;
      final Iterator<Execution> iter = executions(source).iterator();
      while(iter.hasNext()){
         if(iter.next().isDone()){
            iter.remove();
            removed++;
         }
      }
      if(removed == 1){
         source.send("Removed 1 job");
      }else{
         source.send(String.format("Removed %d jobs", removed));
      }
   }
}
