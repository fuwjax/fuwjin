package org.fuwjin.gravitas.config;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.ClassUtils.getWrapper;

import org.fuwjin.gravitas.engine.Command;
import org.fuwjin.pogo.reflect.invoke.Invoker;

public class CommandTarget implements Target{
   private final Command command;
   private final TargetFactory factory;

   public CommandTarget(Command command, TargetFactory factory){
      this.command = command;
      this.factory = factory;
   }

   @Override
   public boolean set(String name, String value){
      Invoker invoker = new Invoker(command.getClass(), name);
      final Class<?> expectedType = invoker.paramTypes(1)[0];
      final Object val;
      if(expectedType.equals(Command.class)){
         val = factory.newCommand(value);
      }else{
         Invoker converter = new Invoker(getWrapper(expectedType), "valueOf");
         val = converter.invoke(null, value);
      }
      final Object result = invoker.invoke(command, val);
      return isSuccess(result);
   }

   @Override
   public Command toCommand(){
      return command;
   }
}
