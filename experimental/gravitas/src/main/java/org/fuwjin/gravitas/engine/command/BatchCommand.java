package org.fuwjin.gravitas.engine.command;

import static org.fuwjin.util.LineIterable.lines;
import static org.fuwjin.util.StreamUtils.open;

import java.io.InputStream;

import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.config.TargetFactory;
import org.fuwjin.gravitas.engine.Command;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class BatchCommand extends Command{
   @Inject
   private Injector injector;
   @Inject
   private GravitasConfig config;
   private String script;

   @Override
   public void doRun() throws Exception{
      final InputStream stream = open(script);
      final TargetFactory factory = config.factory(source());
      try{
         for(final String line: lines(stream)){
            if(line.length() != 0){
               final Command command = factory.newCommand(line);
               command.setSource(source());
               injector.injectMembers(command);
               command.run();
            }
         }
      }finally{
         stream.close();
      }
   }
}
