package org.fuwjin.gravitas;

import static java.lang.System.err;

import org.fuwjin.gravitas.gesture.Integration;

public class BootIntegration implements Integration{
   @Override
   public String name(){
      return "boot";
   }

   @Override
   public void send(final Object... messages){
      for(final Object message: messages){
         err.println(message);
      }
   }
}
