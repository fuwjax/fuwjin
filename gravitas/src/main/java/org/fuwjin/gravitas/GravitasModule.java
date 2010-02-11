package org.fuwjin.gravitas;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static org.fuwjin.gravitas.util.StreamUtils.open;
import static org.fuwjin.pogo.PogoUtils.readGrammar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

import org.fuwjin.gravitas.gesture.GestureProcessor;
import org.fuwjin.gravitas.parser.Parser;
import org.fuwjin.pogo.Grammar;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class GravitasModule extends AbstractModule {
	private final Properties props;

	public GravitasModule(Properties props) {
		this.props = props;
	}

	@Override
	protected void configure() {
		Names.bindProperties(binder(), props);
		bindToBootstrap();
		bind(ScheduledExecutorService.class).toInstance(
				newScheduledThreadPool(5));
	}

   private void bindToBootstrap(){
      Multibinder<Runnable> startCommands = newSetBinder(binder(),
				Runnable.class, named("internal.commands.startup"));
		startCommands.addBinding().to(GestureProcessor.class);
   }

	@Provides
	@Singleton
	protected Parser provideParser(
			@Named("gravitas.command.grammar") String grammarFile)
			throws ParseException, IOException {
		Grammar grammar = readGrammar("gravitas.pogo");
		InputStreamReader reader = new InputStreamReader(open(grammarFile));
		return (Parser) grammar.parse(reader);
	}
}
