package org.fuwjin.gravitas;

import static java.lang.System.out;

import org.fuwjin.gravitas.gesture.Integration;

public class BootIntegration implements Integration{
   @Override
   public void send(Object... messages){
      for(Object message: messages){
         out.println(message);
      }
   }
   
   @Override
   public String name(){
      return "boot";
   }
}
