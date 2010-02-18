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

   public Command newInstance(final String... elements) throws Exception{
      if(atoms.size() != elements.length){
         return null;
      }
      int index = 0;
      final Command runner = (Command)cls.newInstance();
      for(final Token atom: atoms){
         int newIndex = atom.apply(runner, elements, index);
         if(newIndex<0){
            return null;
         }
         index = newIndex+1;
      }
      return runner;
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
