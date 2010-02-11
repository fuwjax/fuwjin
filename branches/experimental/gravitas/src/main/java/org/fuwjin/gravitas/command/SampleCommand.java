package org.fuwjin.gravitas.command;

import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class SampleCommand implements Runnable{
   @Inject
   private Integration reply;
   private String name;
   
   @Override
   public void run(){
      reply.notify(String.format("%s says hi to you!",name));
   }
   
   @Override
	public String toString() {
		return "hello "+name;
	}
}
