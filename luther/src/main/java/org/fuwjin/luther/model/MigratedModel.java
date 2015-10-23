package org.fuwjin.luther.model;

import java.util.List;

import org.fuwjin.luther.Symbol;

public class MigratedModel implements Model {
	private Model model;
	private Symbol symbol;

	public MigratedModel(Symbol symbol, Model model) {
		this.symbol = symbol;
		this.model = model;
	}

	@Override
	public StringBuilder match(StringBuilder builder) {
		return model.match(builder);
	}

	@Override
	public Object value() {
		return model.value();
	}

	@Override
	public Symbol symbol() {
		return symbol;
	}

	@Override
	public List<Node> children() {
		return model.children();
	}
}
