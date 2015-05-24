package org.fuwjin.gravitas.config;

import java.util.List;

import org.fuwjin.gravitas.engine.Command;

public class TargetFactory{
   private final ClassResolver resolver;
   private final ContextConfig config;

   public TargetFactory(final ClassResolver resolver, final ContextConfig context){
      this.resolver = resolver;
      config = context;
   }

   public ContextConfig config(){
      return config;
   }

   public Command newCommand(final String gesture){
      return config.parse(this, gesture);
   }

   public Target newInstance(final String type){
      final Class<?> cls = resolver.forName(type);
      try{
         return new CommandTarget((Command)cls.newInstance(), this);
      }catch(final Exception e){
         return null;
      }
   }

   public Target newMap(final List<Token> targetTokens){
      return new MapTarget(targetTokens, this);
   }
}
