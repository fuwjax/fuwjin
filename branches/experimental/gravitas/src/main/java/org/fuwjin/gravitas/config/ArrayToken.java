package org.fuwjin.gravitas.config;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.StringUtils.join;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.pogo.reflect.invoke.Invoker;

public class ArrayToken implements Token{
   private Invoker invoker;
   private String name;
   private ContextConfig context;
   
   @Override
   public int apply(Command runner, String[] elements, int index) throws Exception{
      StringBuilder builder = new StringBuilder();
      Object sep = join(" ");
      for(int i=index;i<=elements.length;i++){
         builder.append(sep).append(elements[i]);
      }
      Command command = context.parse(builder.toString());
      Object result = invoker.invoke(runner, command);
      return isSuccess(result) ? elements.length - 1 : NOT_APPLIED;
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
