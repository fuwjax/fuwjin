package org.fuwjin.gravitas.engine.command;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class ClearFinishedJobsCommand implements Runnable{
   @Inject
   private ExecutionEngine engine;
   @Inject
   private Integration source;

   @Override
   public void run(){
      final int removed = engine.clear();
      if(removed == 1){
         source.send("Removed 1 job");
      }else{
         source.send(String.format("Removed %d jobs", removed));
      }
   }
}
