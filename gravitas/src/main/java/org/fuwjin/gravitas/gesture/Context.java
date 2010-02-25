package org.fuwjin.gravitas.gesture;

import static org.fuwjin.util.ClassUtils.newProxy;

public class Context implements Integration{
   private final Integration integration;
   private ContextHandler handler;

   public Context(final Integration source){
      integration = source;
   }

   public <T>T adapt(final Class<T> type){
      return newProxy(type, handler());
   }

   public Class<?> getType(){
      return integration.getClass();
   }

   public String name(){
      return integration.name();
   }

   public void send(final Object... messages){
      integration.send(messages);
   }

   private ContextHandler handler(){
      if(handler == null){
         handler = new ContextHandler();
      }
      return handler;
   }
}
