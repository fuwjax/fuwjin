package org.fuwjin.gravitas.console;

import static java.lang.System.in;
import static java.lang.System.out;
import static java.lang.Thread.interrupted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConsoleIntegration implements Integration{
   private final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
   @Inject
   private EventRouter router;
   private final Runnable runner = new Runnable(){
      public void run(){
         readFromIn();
      }
   };

   public ConsoleIntegration(){
      final Thread readThread = new Thread(runner);
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
            final String line = reader.readLine();
            if(line != null){
               router.raise(this, line);
            }
         }
      }catch(final IOException e){
         // continue
      }
   }
}