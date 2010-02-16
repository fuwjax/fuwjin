package org.fuwjin.gravitas.config;

import java.util.HashMap;
import java.util.Map;

public class GravitasConfig{
   private final Map<Class<?>, ContextConfig> contexts = new HashMap<Class<?>, ContextConfig>();
   private ClassResolver resolver;

   public ContextConfig getContext(final Object forObject){
      return contexts.get(forObject.getClass());
   }

   void addContext(final ContextConfig context){
      context.resolve(resolver);
      contexts.put(context.type(), context);
   }
}
