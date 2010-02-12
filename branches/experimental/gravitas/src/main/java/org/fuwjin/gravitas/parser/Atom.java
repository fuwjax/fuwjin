package org.fuwjin.gravitas.parser;

public interface Atom{
   boolean apply(Runnable runner, String value);

   void resolve(Class<?> type);

   String toIdent();
}
