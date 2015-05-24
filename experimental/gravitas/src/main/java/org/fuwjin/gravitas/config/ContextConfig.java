package org.fuwjin.gravitas.config;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.engine.Command;

public class ContextConfig{
   private final List<CommandConfig> commands = new LinkedList<CommandConfig>();
   private String type;

   public Iterable<CommandConfig> commands(){
      return unmodifiableCollection(commands);
   }

   public Command parse(final TargetFactory factory, final String input){
      for(final CommandConfig command: commands){
         final Command task = command.newInstance(factory, input);
         if(task != null){
            return task;
         }
      }
      return null;
   }

   void addCommand(final CommandConfig command){
      commands.add(command);
   }

   String type(){
      return type;
   }
}
