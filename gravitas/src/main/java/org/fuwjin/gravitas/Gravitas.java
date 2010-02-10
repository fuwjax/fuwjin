package org.fuwjin.gravitas;

import java.util.Properties;
import java.util.Set;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.util.StreamUtils;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class Gravitas{
   public static void main(String...args) throws Exception{
      Properties props = new Properties();
      if(args.length == 0){
         props.load(StreamUtils.open("config.properties"));
      }else{
         props.load(StreamUtils.open(args[0]));
      }
      Injector guice = Guice.createInjector(new GravitasModule(props));
      Gravitas gravitas = guice.getInstance(Gravitas.class);
      gravitas.start();
   }

   @Inject
   private ExecutionEngine exec;
   @Inject
   @Named("internal.commands.startup")
   private Set<Runnable> bootstrapCommands;
   
   private void start(){
      for(Runnable command: bootstrapCommands){
         exec.execute(command);
      }
   }
}
