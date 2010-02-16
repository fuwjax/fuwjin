package org.fuwjin.gravitas.config;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

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

   public Runnable newInstance(final String... elements) throws InstantiationException, IllegalAccessException{
      if(atoms.size() != elements.length){
         return null;
      }
      int index = 0;
      final Runnable runner = (Runnable)cls.newInstance();
      for(final Token atom: atoms){
         if(!atom.apply(runner, elements[index++])){
            return null;
         }
      }
      return runner;
   }

   public Class<?> type(){
      return cls;
   }

   void addToken(final Token atom){
      atoms.add(atom);
   }

   void resolve(final ClassResolver resolver){
      cls = resolver.forName(type);
      assert Runnable.class.isAssignableFrom(cls);
      for(final Token atom: atoms){
         atom.resolve(cls);
      }
   }
}
