package org.fuwjin.luther;

import java.io.IOException;

import org.echovantage.util.io.IntReader;

public class Grammar {
	private final Symbol accept;

	public Grammar(final Symbol accept) {
		this.accept = accept;
	}

	public Model parse(final IntReader input) throws IOException {
		return new ParsePosition().parse(accept, input);
	}

	@Override
	public String toString() {
		return accept.toString();
	}
}