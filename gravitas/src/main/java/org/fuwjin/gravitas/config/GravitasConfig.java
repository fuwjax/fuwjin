package org.fuwjin.gravitas.config;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.gravitas.gesture.Context;

public class GravitasConfig{
   private final Map<String, TargetFactory> contexts = new HashMap<String, TargetFactory>();
   private ClassResolver resolver;

   public TargetFactory factory(final Context forObject){
      Class<?> type = forObject.getType();
      if(!resolver.contains(type.getPackage().getName())){
         return null;
      }
      return contexts.get(type.getSimpleName());
   }

   void addContext(final ContextConfig context){
      contexts.put(context.type(), new TargetFactory(resolver,context));
   }
}
