package org.fuwjin.gravitas.console;

import java.io.IOException;

import org.fuwjin.gravitas.engine.RepeatExecution;
import org.fuwjin.gravitas.gesture.GestureRouter;

import com.google.inject.Inject;

@RepeatExecution(waitBetween=20)
public class PollConsole implements Runnable{
   @Inject
   private ConsoleIntegration console;
   @Inject
   private GestureRouter router;
   
   @Override
   public void run(){
      try{
         String line = console.readLine();
         if(line != null){
            router.raise(console, line);
         }
      }catch(IOException e){
         // continue
      }
   }
}
