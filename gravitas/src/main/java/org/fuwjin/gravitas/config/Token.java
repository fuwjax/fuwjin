package org.fuwjin.gravitas.config;

public interface Token{
   boolean apply(Runnable runner, String value);

   void resolve(Class<?> type);

   String toIdent();
}
