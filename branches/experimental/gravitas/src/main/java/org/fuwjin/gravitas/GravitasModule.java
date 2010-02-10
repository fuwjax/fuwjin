package org.fuwjin.gravitas;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static org.fuwjin.pogo.PogoUtils.readGrammar;

import java.text.ParseException;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

import org.fuwjin.gravitas.console.PollConsole;
import org.fuwjin.gravitas.gesture.GestureProcessor;
import org.fuwjin.pogo.Grammar;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class GravitasModule extends AbstractModule{
   private final Properties props;

   public GravitasModule(Properties props){
      this.props = props;
   }
   
   @Override
   protected void configure(){
      Names.bindProperties(binder(), props);
      Multibinder<Runnable> startCommands = newSetBinder(binder(), Runnable.class, named("gravitas.commands.startup"));
      startCommands.addBinding().to(PollConsole.class);
      startCommands.addBinding().to(GestureProcessor.class);
      bind(ScheduledExecutorService.class).toInstance(newScheduledThreadPool(5));
   }
   
   @Provides
   protected Grammar provideGrammar(@Named("gravitas.command.grammar") String grammarFile) throws ParseException{
      return readGrammar(grammarFile);
   }
}
