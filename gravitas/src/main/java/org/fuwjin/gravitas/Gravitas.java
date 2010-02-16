package org.fuwjin.gravitas;

import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.UserInstructionEventHandler;
import org.fuwjin.gravitas.root.RootContext;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;

@Singleton
public class Gravitas{
   public static void main() throws Exception{
      main(new String[0]);
   }
   
   public static void main(final String[] args) throws Exception{
      ServiceLoader<Module> modules = ServiceLoader.load(Module.class);
      List<Module> list = new LinkedList<Module>();
      list.add(new GravitasModule());
      for(Module module: modules){
         list.add(module);
      }
      final Injector guice = Guice.createInjector(list);
      final Gravitas gravitas = guice.getInstance(Gravitas.class);
      gravitas.start();
   }

   @Inject
   private ExecutionEngine engine;
   @Inject
   private UserInstructionEventHandler eventHandler;
   @Inject
   private EventRouter<String> router;

   private void start(){
      engine.execute("*Bootstrap*", eventHandler);
      router.raise(new RootContext(), "run bootstrap.script");
   }
}
