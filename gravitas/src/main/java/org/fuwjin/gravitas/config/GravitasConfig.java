package org.fuwjin.gravitas.config;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.gravitas.gesture.Context;

public class GravitasConfig{
   private final Map<Class<?>, ContextConfig> contexts = new HashMap<Class<?>, ContextConfig>();
   private ClassResolver resolver;

   public ContextConfig configure(final Context forObject){
      return contexts.get(forObject.getType());
   }

   void addContext(final ContextConfig context){
      context.resolve(resolver);
      contexts.put(context.type(), context);
   }
}
