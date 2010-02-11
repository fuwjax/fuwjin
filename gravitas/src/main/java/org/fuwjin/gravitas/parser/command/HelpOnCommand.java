package org.fuwjin.gravitas.parser.command;

import static org.fuwjin.gravitas.util.StringUtils.join;

import org.fuwjin.gravitas.gesture.Integration;
import org.fuwjin.gravitas.parser.Atom;
import org.fuwjin.gravitas.parser.Command;
import org.fuwjin.gravitas.parser.Context;
import org.fuwjin.gravitas.parser.Instruction;
import org.fuwjin.gravitas.parser.Parser;

import com.google.inject.Inject;

public class HelpOnCommand implements Runnable{
   @Inject
   private Integration source;
   @Inject
   private Parser parser;
   private String name;
   
   @Override
   public void run(){
      Context context = parser.getContext(source);
      StringBuilder builder = new StringBuilder();
      Object commandSeparator = join("\n\n");
      for(Command command: context.commands()){
         Instruction primary = findPrimary(command);
         if(primary == null){
            continue;
         }
         builder.append(commandSeparator);
         appendInstruction(primary, builder);
         builder.append(" - ");
         Object lineSeparator = join("\n");
         for(String line: command.helpLines()){
            builder.append(lineSeparator).append(line);
         }
         Object aliasSeparator = join("\n   Aliases: ",", ");
         for(Instruction instruction: command.instructions()){
            if(instruction != primary){
               builder.append(aliasSeparator);
               appendInstruction(instruction, builder);
            }
         }
      }
      source.notify(builder);
   }
   
   private Instruction findPrimary(Command command){
      for(Instruction instruction: command.instructions()){
         for(Atom atom: instruction.atoms()){
            if(atom.toIdent().equals(name)){
               return instruction;
            }
         }
      }
      return null;
   }

   private void appendInstruction(Instruction instruction, StringBuilder builder){
      Object atomSeparator = join(" ");
      for(Atom atom: instruction.atoms()){
         builder.append(atomSeparator);
         builder.append(atom.toIdent());
      }
   }
}
