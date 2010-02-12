package org.fuwjin.gravitas;

import static java.lang.Class.forName;
import static org.fuwjin.gravitas.util.StreamUtils.open;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.fuwjin.gravitas.engine.ExecutionEngine;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class Gravitas{
   public static void main() throws Exception{
      main(new String[0]);
   }
   
   public static void main(final String[] args) throws Exception{
      final Properties props = loadProperties(args);
      final Set<Module> modules = loadModules(props);
      final Injector guice = Guice.createInjector(modules);
      final Gravitas gravitas = guice.getInstance(Gravitas.class);
      gravitas.start();
   }

   private static Set<Module> loadModules(final Properties props) throws InstantiationException,
         IllegalAccessException, ClassNotFoundException{
      final Set<Module> modules = new HashSet<Module>();
      modules.add(new GravitasModule(props));
      for(final Map.Entry<Object, Object> entry: props.entrySet()){
         if(entry.getKey().toString().startsWith("gravitas.module.")){
            final String moduleType = entry.getValue().toString();
            modules.add((Module)forName(moduleType).newInstance());
         }
      }
      return modules;
   }

   private static Properties loadProperties(final String... args) throws IOException, FileNotFoundException{
      final Properties props = new Properties();
      if(args.length == 0){
         props.load(open("config.properties"));
      }else{
         props.load(open(args[0]));
      }
      return props;
   }

   @Inject
   @Named("internal.commands.startup")
   private Set<Runnable> bootstrapCommands;
   @Inject
   private ExecutionEngine exec;

   private void start(){
      for(final Runnable command: bootstrapCommands){
         exec.execute("*bootstrap*", command);
      }
   }
}
