package org.fuwjin.gravitas;

import static java.lang.System.err;

import org.fuwjin.gravitas.gesture.Context;

public class BootIntegration extends Context{
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
