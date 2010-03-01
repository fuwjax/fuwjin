package org.fuwjin.gravitas.engine;

import java.util.concurrent.LinkedBlockingDeque;

import com.google.inject.Inject;

public interface ExecutionContext{
   @Inject
   void executions(LinkedBlockingDeque<Command> commands);
   LinkedBlockingDeque<Command> executions();
}
