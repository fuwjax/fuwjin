package org.fuwjin.gravitas.engine;

import java.util.concurrent.LinkedBlockingDeque;

public interface ExecutionContext{
   LinkedBlockingDeque<Execution> executions();
}
