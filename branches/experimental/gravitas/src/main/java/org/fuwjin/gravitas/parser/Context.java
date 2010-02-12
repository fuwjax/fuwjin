package org.fuwjin.gravitas.parser;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.gesture.Integration;

public class Context{
   private Class<?> cls;
   private final List<Command> commands = new LinkedList<Command>();
   private String type;

   public Iterable<Command> commands(){
      return unmodifiableCollection(commands);
   }

   public Runnable parse(final String input) throws InstantiationException, IllegalAccessException{
      final String[] split = input.split(" ");
      for(final Command command: commands){
         final Runnable task = command.newInstance(split);
         if(task != null){
            return task;
         }
      }
      return null;
   }

   void addCommand(final Command command){
      commands.add(command);
   }

   void resolve(final ClassResolver resolver){
      cls = resolver.forName(type);
      assert Integration.class.isAssignableFrom(cls);
      for(final Command command: commands){
         command.resolve(resolver);
      }
   }

   Class<?> type(){
      return cls;
   }
}
