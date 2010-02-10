package org.fuwjin.gravitas.gesture;

import java.text.ParseException;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Pogo;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class GestureProcessor extends GestureHandler{
   @Inject
   private Grammar grammar;
   @Inject
   private Injector injector;
   @Inject
   private ExecutionEngine engine;
   
   @Override
   public boolean handle(final Integration source, Object gesture){
      if(!(gesture instanceof String)){
         return false;
      }
      Pogo parser = grammar.get(source.getClass().getSimpleName());
      try{
         Runnable command = (Runnable)parser.parse((String)gesture);
         injector.createChildInjector(new AbstractModule(){
            @Override
            protected void configure(){
               bind(Integration.class).toInstance(source);
            }
         }).injectMembers(command);
         engine.execute(command);
         return true;
      }catch(ParseException e){
         return false;
      }
   }
}
