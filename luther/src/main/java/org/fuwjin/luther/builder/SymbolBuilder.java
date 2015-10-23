package org.fuwjin.luther.builder;

import java.util.Objects;
import java.util.Set;

import org.fuwjin.luther.Symbol;

public class SymbolBuilder {
	public Symbol build() {
		if (symbol == null) {
			symbol = new Symbol(name);
			symbol.init(start.build(), start.walk());
		}
		return symbol;
	}

	private Symbol symbol;
	private final String name;
	private final SymbolStateBuilder start;
	private boolean rightCycle;
	private Boolean nullable;
	private boolean checking;

	public SymbolBuilder(final String name) {
		this.name = name;
		start = new SymbolStateBuilder(this);
	}

	public SymbolStateBuilder start() {
		return start;
	}

	public boolean isRightCycle() {
		return rightCycle;
	}

	public boolean isNullable() {
		return nullable;
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final SymbolBuilder o = (SymbolBuilder) obj;
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
		return start.walk();
	}

	public Boolean checkNullable() {
		if (nullable == null) {
			if (checking) {
				return null;
			}
			checking = true;
			nullable = start.checkNullable();
			checking = false;
		}
		return nullable;
	}

	public void collapse() {
		start.collapse();
	}

	public boolean checkRightCycle() {
		if (checking) {
			return true;
		}
		checking = true;
		rightCycle = start.checkRightCycle();
		checking = false;
		return false;
	}

	public void checkRightRoot() {
		start.checkRightRoot();
	}

	public String name() {
		return name;
	}

	public Set<SymbolBuilder> buildPredict() {
		return start.buildPredict();
	}
}