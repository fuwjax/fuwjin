package org.fuwjin.gravitas.gesture;

public interface Integration{
   String name();

   void send(Object... messages);
}
