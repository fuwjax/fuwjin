package org.fuwjin.gravitas.parser;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

public class Command{
   private final List<String> help = new LinkedList<String>();
   private final List<Instruction> instructions = new LinkedList<Instruction>();

   public Iterable<String> helpLines(){
      return unmodifiableCollection(help);
   }

   public Iterable<Instruction> instructions(){
      return unmodifiableCollection(instructions);
   }

   public Runnable newInstance(final String[] elements) throws InstantiationException, IllegalAccessException{
      for(final Instruction instruction: instructions){
         final Runnable runner = instruction.newInstance(elements);
         if(runner != null){
            return runner;
         }
      }
      return null;
   }

   public void resolve(final ClassResolver resolver){
      for(final Instruction instruction: instructions){
         instruction.resolve(resolver);
      }
   }

   void addHelp(final String line){
      help.add(line);
   }

   void addInstruction(final Instruction instruction){
      instructions.add(instruction);
   }
}
