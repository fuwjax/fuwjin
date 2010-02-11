package org.fuwjin.gravitas.gesture;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.parser.Context;
import org.fuwjin.gravitas.parser.Parser;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class GestureProcessor extends GestureHandler{
   @Inject
   private Parser parser;
   @Inject
   private Injector injector;
   @Inject
   private ExecutionEngine engine;
   
   @Override
   public boolean handle(final Integration source, Object gesture){
      if(!(gesture instanceof String)){
         return false;
      }
      Context context = parser.getContext(source);
      try{
         Runnable command = context.parse((String)gesture);
         injector.createChildInjector(new AbstractModule(){
            @Override
            protected void configure(){
               bind(Integration.class).toInstance(source);
            }
         }).injectMembers(command);
         engine.execute(gesture,command);
         return true;
      }catch(Exception e){
         return false;
      }
   }

   @Override
	public String toString() {
		return "*Gesture Processor*";
	}
}
