package org.fuwjin.gravitas.engine.command;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Context;

import com.google.inject.Inject;

public class QuitCommand implements Runnable{
   private long delay;
   @Inject
   private ExecutionEngine engine;
   @Inject
   private Context source;

   public QuitCommand(){
   }

   private QuitCommand(final ExecutionEngine engine, final Context source){
      this.engine = engine;
      this.source = source;
   }

   @Override
   public void run(){
      if(delay == 0){
         source.send("Shutting down now");
         engine.shutdown();
      }else{
         source.send(String.format("Shutting down in %s seconds", delay));
         engine.execute(source, "*delayed quit*", new QuitCommand(engine, source), delay, -1, -1, SECONDS);
      }
   }
}
