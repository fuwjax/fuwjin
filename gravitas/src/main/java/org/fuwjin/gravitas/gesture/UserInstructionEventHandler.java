package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.config.ContextConfig;
import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class UserInstructionEventHandler extends EventHandler<String>{
   @Inject
   private ExecutionEngine engine;
   @Inject
   private Injector injector;
   @Inject
   private GravitasConfig parser;

   @Override
   public boolean handle(final Integration source, final String gesture) throws Exception{
      final ContextConfig context = parser.getContext(source);
      final Runnable command = context.parse((String)gesture);
      injector.createChildInjector(new AbstractModule(){
         @Override
         protected void configure(){
            bind(Integration.class).toInstance(source);
         }
      }).injectMembers(command);
      engine.execute(gesture, command);
      return true;
   }

   @Override
   public String toString(){
      return "*Gesture Processor*";
   }
}
