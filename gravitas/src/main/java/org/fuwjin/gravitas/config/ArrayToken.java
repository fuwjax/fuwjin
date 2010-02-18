package org.fuwjin.gravitas.config;


public class ArrayToken implements Token{
   private String name;
   
   @Override
   public int apply(Target target, String elements, int index) {
      if(target.set(name, elements.substring(index))){
         return elements.length();
      }
      return NOT_APPLIED;
   }

   @Override
   public String toIdent(){
      return "$["+name+"]";
   }
}
