package org.fuwjin.gravitas.config;

import org.fuwjin.gravitas.engine.Command;

public class TargetFactory{
   private ClassResolver resolver;
   private ContextConfig context;
   
   public TargetFactory(ClassResolver resolver, ContextConfig context){
      this.resolver = resolver;
      this.context = context;
   }

   public Target newInstance(String type){
      Class<?> cls = resolver.forName(type);
      try{
         return new CommandTarget((Command)cls.newInstance(), context);
      }catch(Exception e){
         return null;
      }
   }
}
