package org.fuwjin.luther.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.fuwjin.luther.Model;
import org.fuwjin.luther.Node;
import org.fuwjin.luther.impl.Symbol;

/**
 * Created by fuwjax on 2/4/15.
 */
public class StandardModel implements Model {
	private final Symbol symbol;
	private final List<Node> children;
	
	public StandardModel(Model model){
		this.symbol = model.symbol();
		this.children = model.children().stream().map(Node::result).collect(Collectors.toList());
	}

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
	public Object value() {
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final StandardModel o = (StandardModel) obj;
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
