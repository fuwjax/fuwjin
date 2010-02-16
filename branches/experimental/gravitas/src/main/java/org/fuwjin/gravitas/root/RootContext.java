package org.fuwjin.gravitas.root;

import static java.lang.System.out;

import org.fuwjin.gravitas.gesture.Integration;

public class RootContext implements Integration{
   @Override
   public void send(Object... messages){
      for(Object message: messages){
         out.println(message);
      }
   }
}
