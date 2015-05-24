package org.fuwjin.gravitas.config;

import static org.fuwjin.util.StringUtils.join;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuwjin.gravitas.engine.Command;

public class MapTarget implements Target{
   private final Map<String, String> values;
   private final List<Token> tokens;
   private final TargetFactory factory;

   public MapTarget(final List<Token> tokens, final TargetFactory factory){
      this.tokens = tokens;
      this.factory = factory;
      values = new HashMap<String, String>();
   }

   @Override
   public boolean set(final String name, final String value){
      values.put(name, value);
      return true;
   }

   @Override
   public Command toCommand(){
      final StringBuilder builder = new StringBuilder();
      final Object sep = join(" ");
      for(final Token token: tokens){
         builder.append(sep).append(token.value(values));
      }
      return factory.newCommand(builder.toString());
   }
}
