package org.fuwjin.gravitas.config.command;

import static org.fuwjin.util.StringUtils.join;

import org.fuwjin.gravitas.config.CommandConfig;
import org.fuwjin.gravitas.config.ContextConfig;
import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.config.InstructionConfig;
import org.fuwjin.gravitas.config.Token;
import org.fuwjin.gravitas.engine.Command;

import com.google.inject.Inject;

public class HelpCommand extends Command{
   @Inject
   private GravitasConfig parser;

   @Override
   public void doRun(){
      final ContextConfig context = parser.factory(source()).config();
      final StringBuilder builder = new StringBuilder();
      final Object commandSeparator = join("\n");
      for(final CommandConfig command: context.commands()){
         builder.append(commandSeparator);
         final Object instructionSeparator = join(", ");
         for(final InstructionConfig instruction: command.instructions()){
            builder.append(instructionSeparator);
            final Object atomSeparator = join(" ");
            for(final Token atom: instruction.atoms()){
               builder.append(atomSeparator);
               builder.append(atom.toIdent());
            }
         }
         builder.append(" - ").append(command.helpLines().iterator().next());
      }
      source().send(builder);
   }
}
