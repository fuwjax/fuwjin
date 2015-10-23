package org.fuwjin.luther;

import java.util.List;

public interface Model extends Node {
	Symbol symbol();

	List<Node> children();

	@Override
	default StringBuilder match(final StringBuilder builder) {
		children().forEach(node -> node.match(builder));
		return builder;
	}
}
