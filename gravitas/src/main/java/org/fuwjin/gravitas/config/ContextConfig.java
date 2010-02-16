package org.fuwjin.gravitas.config;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.gesture.Integration;

public class ContextConfig{
   private Class<?> cls;
   private final List<CommandConfig> commands = new LinkedList<CommandConfig>();
   private String type;

   public Iterable<CommandConfig> commands(){
      return unmodifiableCollection(commands);
   }

   public Runnable parse(final String input) throws InstantiationException, IllegalAccessException{
      final String[] split = input.split(" ");
      for(final CommandConfig command: commands){
         final Runnable task = command.newInstance(split);
         if(task != null){
            return task;
         }
      }
      return null;
   }

   void addCommand(final CommandConfig command){
      commands.add(command);
   }

   void resolve(final ClassResolver resolver){
      cls = resolver.forName(type);
      assert Integration.class.isAssignableFrom(cls);
      for(final CommandConfig command: commands){
         command.resolve(resolver);
      }
   }

   Class<?> type(){
      return cls;
   }
}
