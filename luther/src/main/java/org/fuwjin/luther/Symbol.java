package org.fuwjin.luther;

import java.util.Objects;
import java.util.function.Supplier;

import org.fuwjin.sample.StandardModel;

public class Symbol {
	private final String name;
	private SymbolState start;
	private String toString;
	private Supplier<StandardModel> modelSupplier;

	Symbol(final String name) {
		this.name = name;
	}

	void init(final SymbolState start, final String toString, final Supplier<StandardModel> modelSupplier) {
		this.start = start;
		this.toString = toString;
		this.modelSupplier = modelSupplier;
	}

	public SymbolState start() {
		return start;
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final Symbol o = (Symbol) obj;
			return Objects.equals(name, o.name);
		} catch (final Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	@Override
	public String toString() {
		return toString;
	}

	public String name() {
		return name;
	}

	public StandardModel model() {
		return modelSupplier.get();
	}
}