package org.fuwjin.gravitas.gesture.command;

import org.fuwjin.gravitas.engine.Command;

public class LoopbackCommand extends Command{
   private String text;
   
   @Override
   protected void doRun(){
      source().send(text);
   }
}
