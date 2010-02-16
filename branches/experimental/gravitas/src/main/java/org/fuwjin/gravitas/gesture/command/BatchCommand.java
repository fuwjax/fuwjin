package org.fuwjin.gravitas.gesture.command;

import static org.fuwjin.util.StreamUtils.open;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class BatchCommand implements Runnable{
   @Inject
   private EventRouter router;
   @Inject
   private Integration source;
   private String script;
   @Override
   public void run(){
      try{
         BufferedReader reader = new BufferedReader(new InputStreamReader(open(script)));
         try{
            String line = "";
            do{
               if(line.length()!=0){
                  router.raise(source, line);
               }
               line = reader.readLine();
            }while(line != null);
         }finally{
            reader.close();
         }
      }catch(IOException e){
         throw new RuntimeException(e);
      }
   }
}
