package org.fuwjin.luther.model;

public interface Node {
	StringBuilder match(StringBuilder builder);

	default String match() {
		return match(new StringBuilder()).toString();
	}
	
	Object value();
	
	Node result();
}