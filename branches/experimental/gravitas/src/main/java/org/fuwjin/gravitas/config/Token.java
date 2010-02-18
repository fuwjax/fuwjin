package org.fuwjin.gravitas.config;

import org.fuwjin.gravitas.engine.Command;

public interface Token{
   int NOT_APPLIED = -1;

   void resolve(ContextConfig context, Class<?> type);

   String toIdent();

   int apply(Command runner, String elements, int index);
}
