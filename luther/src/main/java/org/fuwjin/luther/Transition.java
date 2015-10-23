package org.fuwjin.luther;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class Transition implements Model {
	private final SymbolState state;
	private final Origin orig;
	private final Node[] children;
	private final Transition insertion;
	private Transition alternative;

	public Transition(final Symbol s, final Origin orig) {
		this(s.start(), orig, new Node[0], null);
	}

	private Transition(final SymbolState state, final Origin orig, final Node[] children, final Transition insertion) {
		this.state = state;
		this.orig = orig;
		this.children = children;
		this.insertion = insertion;
	}

	@Override
	public Symbol symbol() {
		return state.lhs();
	}

	@Override
	public List<Node> children() {
		return Arrays.asList(children);
	}

	private Transition accept(final Transition child) {
		final SymbolState to = state.accept(child.symbol());
		return to == null ? null : append(to, child, insertion);
	}

	public Transition accept(final int a) {
		final SymbolState to = state.accept(a);
		return to == null ? null : append(to, new Char(a), insertion);
	}

	private Transition append(final Node node, final Transition insertionPoint) {
		return append(state, node, insertionPoint);
	}

	public Transition markOf(final Transition result) {
		return append(state, result, result);
	}

	private Transition append(final SymbolState to, final Node node, final Transition insertionPoint) {
		if (insertion == null) {
			return add(to, node, insertionPoint);
		}
		final Node[] newChildren = Arrays.copyOf(children, children.length);
		newChildren[children.length - 1] = ((Transition) newChildren[children.length - 1]).append(node, insertionPoint);
		return new Transition(to, orig, newChildren, insertionPoint);
	}

	private Transition add(final SymbolState to, final Node value, final Transition insertPoint) {
		final Node[] newChildren = Arrays.copyOf(children, children.length + 1);
		newChildren[children.length] = value;
		return new Transition(to, orig, newChildren, insertPoint);
	}

	public Set<Symbol> predict() {
		return state.predict();
	}

	public Set<Symbol> pending() {
		return state.pending();
	}

	public Symbol rightCycle() {
		return state.rightCycle();
	}

	@Override
	public String toString() {
		return "[" + state + ", " + orig + "]" + children();
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			final Transition o = (Transition) obj;
			return state.equals(o.state) && orig.equals(o.orig);
		} catch (final Exception e) {
			try {
				final Model o = (Model) obj;
				return symbol().equals(o.symbol()) && children().equals(o.children());
			} catch (final Exception ex) {
				return false;
			}
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(state, orig);
	}

	public void addAlternative(final Transition next) {
		if (alternative == null) {
			alternative = next;
		} else {
			alternative.addAlternative(next);
		}
	}

	public void complete(final Consumer<Transition> parsePosition) {
		if (state.isComplete()) {
			orig.complete(mark -> parsePosition.accept(mark.accept(this)));
		}
	}

	public Transition mark() {
		return orig.markOf(this);
	}

	public boolean isModelFor(final Symbol symbol) {
		return symbol.equals(state.lhs()) && state.isComplete();
	}
}