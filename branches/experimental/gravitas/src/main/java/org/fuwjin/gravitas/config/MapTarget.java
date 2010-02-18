package org.fuwjin.gravitas.config;

import static org.fuwjin.util.StringUtils.join;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuwjin.gravitas.engine.Command;

public class MapTarget implements Target{
   private Map<String,String> values;
   private final List<Token> tokens;
   private final TargetFactory factory;
   public MapTarget(List<Token> tokens, TargetFactory factory){
      this.tokens = tokens;
      this.factory = factory;
      values= new HashMap<String, String>();
   }

   @Override
   public boolean set(String name, String value){
      values.put(name, value);
      return true;
   }

   @Override
   public Command toCommand(){
      StringBuilder builder = new StringBuilder();
      Object sep = join(" ");
      for(Token token: tokens){
         builder.append(sep).append(token.value(values));
      }
      return factory.parse(builder.toString());
   }
}
