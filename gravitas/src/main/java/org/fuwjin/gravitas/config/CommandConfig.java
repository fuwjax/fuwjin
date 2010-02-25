package org.fuwjin.gravitas.config;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.engine.Command;

public class CommandConfig{
   private final List<String> help = new LinkedList<String>();
   private final List<InstructionConfig> instructions = new LinkedList<InstructionConfig>();

   public Iterable<String> helpLines(){
      return unmodifiableCollection(help);
   }

   public Iterable<InstructionConfig> instructions(){
      return unmodifiableCollection(instructions);
   }

   public Command newInstance(final TargetFactory factory, final String gesture){
      for(final InstructionConfig instruction: instructions){
         final Command runner = instruction.newInstance(factory, gesture);
         if(runner != null){
            return runner;
         }
      }
      return null;
   }

   void addHelp(final String line){
      help.add(line);
   }

   void addInstruction(final InstructionConfig instruction){
      instructions.add(instruction);
   }
}
