package org.fuwjin.gravitas.gesture.handler;

import java.util.concurrent.ScheduledFuture;

import org.fuwjin.gravitas.config.ContextConfig;
import org.fuwjin.gravitas.config.GravitasConfig;
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
   private GravitasConfig parser;
   @Inject
   private ExecutionContextHelper helper;

   @Override
   public boolean handle(final Event event) throws Exception{
      final ContextConfig context = parser.configure(event.source());
      final Command command = context.parse((String)event.gesture());
      command.setSource(event.source());
      injector.injectMembers(command);
      ScheduledFuture<?> future = engine.execute(command);
      helper.storeExecution(event.source(), command, future);
      return true;
   }
}
