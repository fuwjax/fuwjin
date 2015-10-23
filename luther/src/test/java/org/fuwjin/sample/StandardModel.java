package org.fuwjin.sample;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.fuwjin.luther.Model;
import org.fuwjin.luther.Node;
import org.fuwjin.luther.Symbol;

/**
 * Created by fuwjax on 2/4/15.
 */
public class StandardModel implements Model, Node {
	private final Symbol symbol;
	private final List<Node> children;

	public StandardModel(final Symbol symbol, final Node... children) {
		this.symbol = symbol;
		this.children = Arrays.asList(children);
	}

	@Override
	public List<Node> children() {
		return children;
	}

	@Override
	public Symbol symbol() {
		return symbol;
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final Model o = (Model) obj;
			return symbol.equals(o.symbol()) && children().equals(o.children());
		} catch (final Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol, children());
	}

	@Override
	public String toString() {
		return symbol.name() + children();
	}
}
