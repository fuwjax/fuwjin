package org.fuwjin.luther;

public interface Node {
	StringBuilder match(StringBuilder builder);

	default String match() {
		return match(new StringBuilder()).toString();
	}
}