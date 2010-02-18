package org.fuwjin.gravitas.config;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.gravitas.gesture.Context;

public class GravitasConfig{
   private final Map<String, ContextConfig> contexts = new HashMap<String, ContextConfig>();
   private ClassResolver resolver;

   public ContextConfig configure(final Context forObject){
      Class<?> type = forObject.getType();
      if(!resolver.contains(type.getPackage().getName())){
         return null;
      }
      return contexts.get(type.getSimpleName());
   }

   void addContext(final ContextConfig context){
      context.setResolver(resolver);
      contexts.put(context.type(), context);
   }
}
