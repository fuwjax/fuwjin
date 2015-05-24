package org.fuwjin.gravitas.config;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.engine.Command;

public class InstructionConfig{
   private final List<Token> atoms = new LinkedList<Token>();
   private TargetStrategy strategy;

   public Iterable<Token> atoms(){
      return unmodifiableCollection(atoms);
   }

   public Command newInstance(final TargetFactory factory, final String gesture){
      String remaining = gesture;
      final Target target = strategy.newTarget(factory);
      for(final Token atom: atoms){
         remaining = atom.apply(target, remaining);
         if(remaining == null){
            return null;
         }
      }
      if(remaining.length() != 0){
         return null;
      }
      final Command command = target.toCommand();
      command.setGesture(gesture);
      return command;
   }

   void addToken(final Token atom){
      atoms.add(atom);
   }
}
