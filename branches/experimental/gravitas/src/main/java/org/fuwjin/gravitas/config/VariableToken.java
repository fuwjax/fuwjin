package org.fuwjin.gravitas.config;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.StringUtils.word;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.pogo.reflect.invoke.Invoker;
import org.fuwjin.util.ClassUtils;

public class VariableToken implements Token{
   private Invoker converter;
   private Invoker invoker;
   private String name;
   
   @Override
   public int apply(Command runner, String elements, int index){
      String word = word(elements, index);
      final Object val = converter.invoke(null, word.trim());
      final Object result = invoker.invoke(runner, val);
      return isSuccess(result) ? index + word.length() : NOT_APPLIED;
   }

   @Override
   public void resolve(ContextConfig context, Class<?> type){
      invoker = new Invoker(type, name);
      final Class<?> expectedType = invoker.paramTypes(1)[0];
      converter = new Invoker(ClassUtils.getWrapper(expectedType), "valueOf");
   }

   @Override
   public String toIdent(){
      return '$' + name;
   }
}
