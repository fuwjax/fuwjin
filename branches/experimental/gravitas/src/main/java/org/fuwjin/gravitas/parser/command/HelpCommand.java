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
   private Parser parser;
   @Inject
   private Integration source;

   @Override
   public void run(){
      final Context context = parser.getContext(source);
      final StringBuilder builder = new StringBuilder();
      final Object commandSeparator = join("\n");
      for(final Command command: context.commands()){
         builder.append(commandSeparator);
         final Object instructionSeparator = join(", ");
         for(final Instruction instruction: command.instructions()){
            builder.append(instructionSeparator);
            final Object atomSeparator = join(" ");
            for(final Atom atom: instruction.atoms()){
               builder.append(atomSeparator);
               builder.append(atom.toIdent());
            }
         }
         builder.append(" - ").append(command.helpLines().iterator().next());
      }
      source.notify(builder);
   }
}
