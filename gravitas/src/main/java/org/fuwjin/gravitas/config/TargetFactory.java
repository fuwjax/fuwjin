package org.fuwjin.gravitas.config;

import java.util.List;

import org.fuwjin.gravitas.engine.Command;

public class TargetFactory{
   private ClassResolver resolver;
   private ContextConfig config;
   
   public TargetFactory(ClassResolver resolver, ContextConfig context){
      this.resolver = resolver;
      this.config = context;
   }

   public Target newInstance(String type){
      Class<?> cls = resolver.forName(type);
      try{
         return new CommandTarget((Command)cls.newInstance(), this);
      }catch(Exception e){
         return null;
      }
   }
   
   public Command newCommand(String gesture){
      return config.parse(this, gesture);
   }
   
   public ContextConfig config(){
      return config;
   }

   public Target newMap(List<Token> targetTokens){
      return new MapTarget(targetTokens, this);
   }
}
