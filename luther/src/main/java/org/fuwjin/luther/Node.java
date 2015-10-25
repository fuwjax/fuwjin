package org.fuwjin.luther;

import org.fuwjin.luther.model.Char;

public interface Node {
	StringBuilder match(StringBuilder builder);

	default String match() {
		return match(new StringBuilder()).toString();
	}

	Object value();

	Node result();

	static Node codepoint(final int ch) {
		return new Char(ch);
	}
}