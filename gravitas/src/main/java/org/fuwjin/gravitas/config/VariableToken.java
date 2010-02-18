package org.fuwjin.gravitas.config;

import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableToken implements Token{
   private String name;
   private static Pattern pattern = compile("(\\S+)\\s*(.*)");
   
   @Override
   public String apply(Target target, String gesture){
      Matcher matcher = pattern.matcher(gesture);
      if(matcher.matches() && target.set(name, matcher.group(1))){
         return matcher.group(2);
      }
      return null;
   }
   
   @Override
   public String value(Map<String, String> values){
      return values.get(name);
   }

   @Override
   public String toIdent(){
      return '$' + name;
   }
}
