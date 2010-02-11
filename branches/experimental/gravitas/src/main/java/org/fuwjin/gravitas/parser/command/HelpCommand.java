package org.fuwjin.gravitas.parser.command;

import static org.fuwjin.gravitas.util.StringUtils.join;

import org.fuwjin.gravitas.gesture.Integration;
import org.fuwjin.gravitas.parser.Atom;
import org.fuwjin.gravitas.parser.Command;
import org.fuwjin.gravitas.parser.Context;
import org.fuwjin.gravitas.parser.Instruction;
import org.fuwjin.gravitas.parser.Parser;

import com.google.inject.Inject;

public class HelpCommand implements Runnable{
   @Inject
   private Integration source;
   @Inject
   private Parser parser;
   
   @Override
   public void run(){
      Context context = parser.getContext(source);
      StringBuilder builder = new StringBuilder();
      Object commandSeparator = join("\n");
      for(Command command: context.commands()){
         builder.append(commandSeparator);
         Object instructionSeparator = join(", ");
         for(Instruction instruction: command.instructions()){
            builder.append(instructionSeparator);
            Object atomSeparator = join(" ");
            for(Atom atom: instruction.atoms()){
               builder.append(atomSeparator);
               builder.append(atom.toIdent());
            }
         }
         builder.append(" - ").append(command.helpLines().iterator().next());
      }
      source.notify(builder);
   }
}
