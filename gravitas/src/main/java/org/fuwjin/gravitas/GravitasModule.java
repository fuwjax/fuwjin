package org.fuwjin.gravitas;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static org.fuwjin.pogo.PogoUtils.readGrammar;
import static org.fuwjin.util.StreamUtils.open;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.concurrent.ScheduledExecutorService;

import org.fuwjin.gravitas.config.GravitasConfig;
import org.fuwjin.pogo.Grammar;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class GravitasModule extends AbstractModule{
   @Override
   protected void configure(){
      bind(ScheduledExecutorService.class).toInstance(newScheduledThreadPool(5));
   }

   @Provides
   @Singleton
   protected GravitasConfig provideConfig() throws ParseException, IOException{
      final Grammar grammar = readGrammar("gravitas.pogo");
      final InputStreamReader reader = new InputStreamReader(open("gravitas.config"));
      return (GravitasConfig)grammar.parse(reader);
   }
}
