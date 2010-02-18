package org.fuwjin.gravitas.gesture.command;

import static org.fuwjin.util.LineIterable.lines;
import static org.fuwjin.util.StreamUtils.open;

import java.io.IOException;
import java.io.InputStream;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class BatchCommand extends Command{
   @Inject
   private EventRouter router;
   private String script;

   @Override
   public void doRun(){
      try{
         execScript(router, source(), script);
      }catch(IOException e){
         throw new RuntimeException(e);
      }
   }

   public static void execScript(EventRouter router, Integration source, String script) throws IOException{
      InputStream stream = open(script);
      try{
         for(String line: lines(stream)){
            if(line.length() != 0){
               router.raise(source, line);
            }
         }
      }finally{
         stream.close();
      }
   }
}
