package org.fuwjin.gravitas;

import static com.google.inject.Guice.createInjector;
import static java.util.ServiceLoader.load;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.gravitas.engine.ExecutionEngine;
import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.UserInstructionEventHandler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;

@Singleton
public class Gravitas{
   public static void main(final String... args) throws Exception{
      startGravitas(load(Module.class));
   }

   public static Injector startGravitas(Iterable<Module> modules){
      List<Module> list = new LinkedList<Module>();
      list.add(new GravitasModule());
      for(Module module: modules){
         list.add(module);
      }
      final Injector guice = createInjector(list);
      final Gravitas gravitas = guice.getInstance(Gravitas.class);
      gravitas.start();
      return guice;
   }

   @Inject
   private ExecutionEngine engine;
   @Inject
   private UserInstructionEventHandler eventHandler;
   @Inject
   private EventRouter router;

   private void start(){
      BootIntegration source = new BootIntegration();
      engine.execute(router.getContext(source),"*Bootstrap*", eventHandler);
      router.raise(source, "run bootstrap.script");
   }
}
