package org.fuwjin.gravitas.gesture;

import static org.fuwjin.util.ClassUtils.newProxy;


public class Context implements Integration{
   private Integration integration;
   private ContextHandler handler;
   
   public Context(Integration source){
      integration = source;
   }

   public void send(Object... messages){
      integration.send(messages);
   }
   
   public String name(){
      return integration.name();
   }
   
   public <T> T adapt(Class<T> type){
      return newProxy(type, handler());
   }

   private ContextHandler handler(){
      if(handler == null){
         handler = new ContextHandler();
      }
      return handler;
   }
   
   public Class<?> getType(){
      return integration.getClass();
   }
}
