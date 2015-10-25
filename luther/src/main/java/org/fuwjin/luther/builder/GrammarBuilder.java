package org.fuwjin.luther.builder;

import static java.util.function.Function.identity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fuwjin.luther.Grammar;
import org.fuwjin.luther.Model;
import org.fuwjin.luther.impl.GrammarImpl;

public class GrammarBuilder {
	private final Map<String, SymbolBuilder> symbols = new HashMap<>();
	private final Map<String, Function<Model, ?>> transforms;

	public GrammarBuilder() {
		this(Collections.emptyMap());
	}

	public GrammarBuilder(final Map<String, Function<Model, ?>> transforms) {
		this.transforms = transforms;
	}

	public Function<Model, ?> transform(final String name) {
		return transforms.getOrDefault(name, identity());
	}

	public SymbolBuilder symbol(final String name) {
		SymbolBuilder symbol = symbols.get(name);
		if (symbol == null) {
			symbol = new SymbolBuilder(name);
			symbols.put(name, symbol);
		}
		return symbol;
	}

	public Grammar build(final String start) {
		for (final SymbolBuilder s : symbols.values()) {
			s.checkNullable();
		}
		for (final SymbolBuilder s : symbols.values()) {
			s.collapse();
		}
		for (final SymbolBuilder s : symbols.values()) {
			s.buildPredict();
		}
		for (final SymbolBuilder s : symbols.values()) {
			s.checkRightCycle();
		}
		for (final SymbolBuilder s : symbols.values()) {
			s.checkRightRoot();
		}
		return new GrammarImpl(symbol(start));
	}
}