package org.fuwjin.gravitas.console;

import com.google.inject.AbstractModule;

public class ConsoleModule extends AbstractModule{
   @Override
   protected void configure(){
      bind(ConsoleIntegration.class).asEagerSingleton();
   }
}
