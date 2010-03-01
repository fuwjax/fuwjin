package org.fuwjin.gravitas.console;

import static java.lang.System.in;
import static java.lang.System.out;
import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.fuwjin.gravitas.gesture.Context;
import org.fuwjin.gravitas.gesture.EventRouter;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConsoleIntegration extends Context{
   private final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
   @Inject
   private EventRouter router;
   private final Runnable runner = new Runnable(){
      public void run(){
         readFromIn();
      }
   };

   public ConsoleIntegration(){
      final Thread readThread = new Thread(runner, "Console Reader");
      readThread.setDaemon(true);
      readThread.start();
   }

   @Override
   public void send(final Object... messages){
      for(final Object message: messages){
         out.println(message);
      }
   }

   @Override
   public String name(){
      return "console";
   }

   protected void readFromIn(){
      try{
         while(!interrupted()){
            while(!reader.ready()){
               sleep(20);
            }
            final String line = reader.readLine();
            if(line != null){
               router.raise(this, line);
            }
         }
      }catch(final Exception e){
         // exit thread
      }
   }
}
