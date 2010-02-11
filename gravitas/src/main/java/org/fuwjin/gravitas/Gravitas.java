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
public class Gravitas {
	public static void main(String... args) throws Exception {
		Properties props = loadProperties(args);
		Set<Module> modules = loadModules(props);
		Injector guice = Guice.createInjector(modules);
		Gravitas gravitas = guice.getInstance(Gravitas.class);
		gravitas.start();
	}

	private static Set<Module> loadModules(Properties props)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Set<Module> modules = new HashSet<Module>();
		modules.add(new GravitasModule(props));
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			if ("gravitas.module".equals(entry.getKey())) {
				String moduleType = entry.getValue().toString();
				modules.add((Module)forName(moduleType).newInstance());
			}
		}
		return modules;
	}

	private static Properties loadProperties(String... args)
			throws IOException, FileNotFoundException {
		Properties props = new Properties();
		if (args.length == 0) {
			props.load(open("config.properties"));
		} else {
			props.load(open(args[0]));
		}
		return props;
	}

	@Inject
	private ExecutionEngine exec;
	@Inject
	@Named("internal.commands.startup")
	private Set<Runnable> bootstrapCommands;

	private void start() {
		for (Runnable command : bootstrapCommands) {
			exec.execute("*bootstrap*",command);
		}
	}
}
