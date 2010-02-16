package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.config.ContextConfig;
import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class UserInstructionEventHandler extends EventHandler{
   @Inject
   private ExecutionEngine engine;
   @Inject
   private Injector injector;
   @Inject
   private GravitasConfig parser;

   @Override
   public boolean handle(final Context source, final Object gesture) throws Exception{
      final ContextConfig context = parser.configure(source);
      final Runnable command = context.parse((String)gesture);
      injector.createChildInjector(new AbstractModule(){
         @Override
         protected void configure(){
            bind(Context.class).toInstance(source);
            bind(Integration.class).toInstance(source);
         }
      }).injectMembers(command);
      engine.execute(source, gesture, command);
      return true;
   }

   @Override
   public String toString(){
      return "*Gesture Processor*";
   }
}
