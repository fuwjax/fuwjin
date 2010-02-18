package org.fuwjin.gravitas.gesture.handler;

import java.util.concurrent.ScheduledFuture;

import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.config.TargetFactory;
import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.gravitas.engine.ExecutionContextHelper;
import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.Event;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class UserInstructionEventHandler extends AbstractEventHandler{
   @Inject
   private ExecutionEngine engine;
   @Inject
   private Injector injector;
   @Inject
   private GravitasConfig config;
   @Inject
   private ExecutionContextHelper helper;

   @Override
   public boolean handle(final Event event) throws Exception{
      final TargetFactory factory = config.factory(event.source());
      final Command command = factory.parse((String)event.gesture());
      command.setSource(event.source());
      command.inject(injector);
      ScheduledFuture<?> future = engine.execute(command);
      helper.storeExecution(event.source(), command, future);
      return true;
   }
}
