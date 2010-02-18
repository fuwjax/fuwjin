package org.fuwjin.gravitas.config;

import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteralToken implements Token{
   private String value;
   private Pattern pattern;
   
   @Override
   public String apply(Target target, String gesture){
      if(pattern == null){
         pattern = compile("("+value+")\\s*(.*)");
      }
      Matcher matcher = pattern.matcher(gesture);
      if(matcher.matches()){
         return matcher.group(2);
      }
      return null;
   }
   
   @Override
   public String value(Map<String, String> values){
      return value;
   }

   @Override
   public String toIdent(){
      return value;
   }
}
