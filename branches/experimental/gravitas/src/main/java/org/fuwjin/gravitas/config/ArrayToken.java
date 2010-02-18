package org.fuwjin.gravitas.config;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.pogo.reflect.invoke.Invoker;

public class ArrayToken implements Token{
   private Invoker invoker;
   private String name;
   private ContextConfig context;
   
   @Override
   public int apply(Command runner, String elements, int index) {
      Command command = context.parse(elements.substring(index));
      Object result = invoker.invoke(runner, command);
      return isSuccess(result) ? elements.length() : NOT_APPLIED;
   }

   @Override
   public void resolve(ContextConfig context, Class<?> type){
      this.context = context;
      invoker = new Invoker(type, name);
   }

   @Override
   public String toIdent(){
      return "$["+name+"]";
   }
}
