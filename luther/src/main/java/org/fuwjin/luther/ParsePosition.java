package org.fuwjin.luther;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.Set;

import org.echovantage.util.io.IntReader;

public class ParsePosition {
	private Map<Transition, Transition> items = new HashMap<>();
	private Map<Transition, Transition> oldItems = new HashMap<>();
	private final Set<Symbol> predict = new LinkedHashSet<>();
	private final Map<Symbol, Origin> origins = new HashMap<>();
	private int index;

	public Model parse(final Symbol accept, final IntReader input) {
		index = 0;
		final OfInt iter = input.stream().iterator();
		// can't just call save, as we need the predict set from accept
		add(new Transition(accept, origin(accept)));
		while (iter.hasNext()) {
			predict();
			clear();
			acceptNext(iter.nextInt());
		}
		return result(accept);
	}

	private void acceptNext(final int ch) {
		index++;
		oldItems.values().stream().map(item -> item.accept(ch)).forEach(this::add);
	}

	private void clear() {
		final Map<Transition, Transition> temp = oldItems;
		oldItems = items;
		items = temp;
		items.clear();
		predict.clear();
		origins.clear();
	}

	private Origin origin(final Symbol symbol) {
		return origins.computeIfAbsent(symbol, s -> new Origin(index));
	}

	private void add(final Transition next) {
		if (next == null) {
			return;
		}
		if (!items.containsKey(next)) {
			save(next);
			next.complete(this::add);
			next.predict().stream().filter(predict::add).forEach(this::save);
		} else { // grammar is ambiguous
			items.get(next).addAlternative(next);
		}
	}

	private void save(final Symbol symbol) {
		save(new Transition(symbol, origin(symbol)));
	}

	private void save(final Transition item) {
		items.put(item, item);
		for (final Symbol pending : item.pending()) {
			origin(pending).addAwaiting(item);
		}
	}

	private void predict() {
		for (final Transition item : items.values()) {
			final Symbol rr = item.rightCycle();
			if (rr != null) {
				origin(rr).setMark(item);
			}
		}
	}

	private Model result(final Symbol symbol) {
		for (final Transition item : items.values()) {
			if (item.isModelFor(symbol)) {
				return item;
			}
		}
		throw new IllegalArgumentException("Invalid or incomplete input");
	}
}