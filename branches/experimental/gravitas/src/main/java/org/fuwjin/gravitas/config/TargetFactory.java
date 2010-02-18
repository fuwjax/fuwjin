package org.fuwjin.gravitas.config;

import java.util.List;

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
         return new CommandTarget((Command)cls.newInstance(), this);
      }catch(Exception e){
         return null;
      }
   }
   
   public Command parse(String gesture){
      return context.parse(this, gesture);
   }
   
   public ContextConfig config(){
      return context;
   }

   public Target newMap(List<Token> targetTokens){
      return new MapTarget(targetTokens, this);
   }
}
