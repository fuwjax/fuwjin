package org.fuwjin.gravitas.config;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.engine.Command;

public class InstructionConfig{
   private final List<Token> atoms = new LinkedList<Token>();
   private Class<?> cls;
   private String type;

   public Iterable<Token> atoms(){
      return unmodifiableCollection(atoms);
   }

   public String description(){
      try{
         return cls.newInstance().toString();
      }catch(final Exception e){
         return "ERROR: No Description Found";
      }
   }

   public Command newInstance(final String elements){
      int index = 0;
      final Command runner = newInstance();
      for(final Token atom: atoms){
         index = atom.apply(runner, elements, index);
         if(index<0){
            return null;
         }
      }
      if(index < elements.length()){
         return null;
      }
      runner.setGesture(elements);
      return runner;
   }

   private Command newInstance(){
      try{
         return (Command)cls.newInstance();
      }catch(Exception e){
         return null;
      }
   }

   public Class<?> type(){
      return cls;
   }

   void addToken(final Token atom){
      atoms.add(atom);
   }

   void resolve(final ContextConfig context, final ClassResolver resolver){
      cls = resolver.forName(type);
      assert Runnable.class.isAssignableFrom(cls);
      for(final Token atom: atoms){
         atom.resolve(context, cls);
      }
   }
}
