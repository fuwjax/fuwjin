package org.fuwjin.gravitas.config.command;

import static org.fuwjin.util.StringUtils.join;

import org.fuwjin.gravitas.config.CommandConfig;
import org.fuwjin.gravitas.config.ContextConfig;
import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.gravitas.config.InstructionConfig;
import org.fuwjin.gravitas.config.Token;
import org.fuwjin.gravitas.engine.Command;

import com.google.inject.Inject;

public class HelpOnCommand extends Command{
   private String name;
   @Inject
   private GravitasConfig parser;

   @Override
   public void doRun(){
      final ContextConfig context = parser.factory(source()).config();
      final StringBuilder builder = new StringBuilder();
      final Object commandSeparator = join("\n\n");
      for(final CommandConfig command: context.commands()){
         final InstructionConfig primary = findPrimary(command);
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
         for(final InstructionConfig instruction: command.instructions()){
            if(instruction != primary){
               builder.append(aliasSeparator);
               appendInstruction(instruction, builder);
            }
         }
      }
      source().send(builder);
   }

   private void appendInstruction(final InstructionConfig instruction, final StringBuilder builder){
      final Object atomSeparator = join(" ");
      for(final Token atom: instruction.atoms()){
         builder.append(atomSeparator);
         builder.append(atom.toIdent());
      }
   }

   private InstructionConfig findPrimary(final CommandConfig command){
      for(final InstructionConfig instruction: command.instructions()){
         for(final Token atom: instruction.atoms()){
            if(atom.toIdent().equals(name)){
               return instruction;
            }
         }
      }
      return null;
   }
}
