package org.fuwjin.gravitas.config;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.engine.Command;

public class InstructionConfig{
   private final List<Token> atoms = new LinkedList<Token>();
   private String type;

   public Iterable<Token> atoms(){
      return unmodifiableCollection(atoms);
   }

   public Command newInstance(TargetFactory factory, final String elements){
      int index = 0;
      final Target target = factory.newInstance(type);
      for(final Token atom: atoms){
         index = atom.apply(target, elements, index);
         if(index<0){
            return null;
         }
      }
      if(index < elements.length()){
         return null;
      }
      Command command = target.toCommand();
      command.setGesture(elements);
      return command;
   }

   void addToken(final Token atom){
      atoms.add(atom);
   }
}
