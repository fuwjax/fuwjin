package org.fuwjin.gravitas.config;

import java.util.LinkedList;
import java.util.List;

public class TokenTargetStrategy implements TargetStrategy{
   private final List<Token> tokens = new LinkedList<Token>();

   @Override
   public Target newTarget(final TargetFactory factory){
      return factory.newMap(tokens);
   }

   void addToken(final Token atom){
      tokens.add(atom);
   }
}
