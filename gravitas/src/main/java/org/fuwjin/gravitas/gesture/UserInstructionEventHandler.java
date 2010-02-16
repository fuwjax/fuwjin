package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.config.ContextConfig;
import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class UserInstructionEventHandler extends AbstractEventHandler{
   @Inject
   private ExecutionEngine engine;
   @Inject
   private Injector injector;
   @Inject
   private GravitasConfig parser;

   @Override
   public boolean handle(final Event event) throws Exception{
      final ContextConfig context = parser.configure(event.source());
      final Runnable command = context.parse((String)event.gesture());
      injector.createChildInjector(new AbstractModule(){
         @Override
         protected void configure(){
            bind(Context.class).toInstance(event.source());
            bind(Integration.class).toInstance(event.source());
         }
      }).injectMembers(command);
      engine.execute(event.source(), event.gesture(), command);
      return true;
   }

   @Override
   public String toString(){
      return "*Gesture Processor*";
   }
}
