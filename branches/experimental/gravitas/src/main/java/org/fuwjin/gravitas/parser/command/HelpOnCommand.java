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
   private String name;
   @Inject
   private Parser parser;
   @Inject
   private Integration source;

   @Override
   public void run(){
      final Context context = parser.getContext(source);
      final StringBuilder builder = new StringBuilder();
      final Object commandSeparator = join("\n\n");
      for(final Command command: context.commands()){
         final Instruction primary = findPrimary(command);
         if(primary == null){
            continue;
         }
         builder.append(commandSeparator);
         appendInstruction(primary, builder);
         builder.append(" - ");
         final Object lineSeparator = join("\n");
         for(final String line: command.helpLines()){
            builder.append(lineSeparator).append(line);
         }
         final Object aliasSeparator = join("\n   Aliases: ", ", ");
         for(final Instruction instruction: command.instructions()){
            if(instruction != primary){
               builder.append(aliasSeparator);
               appendInstruction(instruction, builder);
            }
         }
      }
      source.notify(builder);
   }

   private void appendInstruction(final Instruction instruction, final StringBuilder builder){
      final Object atomSeparator = join(" ");
      for(final Atom atom: instruction.atoms()){
         builder.append(atomSeparator);
         builder.append(atom.toIdent());
      }
   }

   private Instruction findPrimary(final Command command){
      for(final Instruction instruction: command.instructions()){
         for(final Atom atom: instruction.atoms()){
            if(atom.toIdent().equals(name)){
               return instruction;
            }
         }
      }
      return null;
   }
}
