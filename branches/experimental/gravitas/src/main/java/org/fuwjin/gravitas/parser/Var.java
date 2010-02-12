package org.fuwjin.gravitas.parser;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;

import org.fuwjin.gravitas.util.ClassUtils;
import org.fuwjin.pogo.reflect.invoke.Invoker;

class Var implements Atom{
   private Invoker converter;
   private Invoker invoker;
   private String name;

   @Override
   public boolean apply(final Runnable runner, final String value){
      final Object val = converter.invoke(null, value);
      final Object result = invoker.invoke(runner, val);
      return isSuccess(result);
   }

   @Override
   public void resolve(final Class<?> type){
      invoker = new Invoker(type, name);
      final Class<?> expectedType = invoker.paramTypes(1)[0];
      converter = new Invoker(ClassUtils.getWrapper(expectedType), "valueOf");
   }

   @Override
   public String toIdent(){
      return '$' + name;
   }
}
