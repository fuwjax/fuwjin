package org.fuwjin.gravitas.gesture.handler;

import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.config.TargetFactory;
import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Event;

import com.google.inject.Inject;

public class UserInstructionEventHandler extends AbstractEventHandler{
   @Inject
   private ExecutionEngine engine;
   @Inject
   private GravitasConfig config;

   @Override
   public boolean handle(final Event event) throws Exception{
      final TargetFactory factory = config.factory(event.source());
      final Command command = factory.newCommand((String)event.gesture());
      engine.execute(event.source(), command);
      return true;
   }
}
