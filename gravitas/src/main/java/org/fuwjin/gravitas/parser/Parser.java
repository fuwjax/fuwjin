package org.fuwjin.gravitas.parser;

import java.util.HashMap;
import java.util.Map;

public class Parser{
   private final Map<Class<?>, Context> contexts = new HashMap<Class<?>, Context>();
   private ClassResolver resolver;

   public Context getContext(final Object forObject){
      return contexts.get(forObject.getClass());
   }

   void addContext(final Context context){
      context.resolve(resolver);
      contexts.put(context.type(), context);
   }
}
