package org.fuwjin.gravitas.parser;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

public class Command{
   private List<String> help = new LinkedList<String>();
   private List<Instruction> instructions = new LinkedList<Instruction>();
   void addHelp(String line){
      help.add(line);
   }
   
   void addInstruction(Instruction instruction){
      instructions.add(instruction);
   }

   public Runnable newInstance(String[] elements) throws InstantiationException, IllegalAccessException{
      for(Instruction instruction: instructions){
         Runnable runner = instruction.newInstance(elements);
         if(runner != null){
            return runner;
         }
      }
      return null;
   }
   
   public Iterable<String> helpLines(){
      return unmodifiableCollection(help);
   }
   
   public Iterable<Instruction> instructions(){
      return unmodifiableCollection(instructions);
   }

   public void resolve(ClassResolver resolver){
      for(Instruction instruction: instructions){
         instruction.resolve(resolver);
      }
   }
}
