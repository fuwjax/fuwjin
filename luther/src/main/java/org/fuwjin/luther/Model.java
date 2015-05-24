package org.fuwjin.luther;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface Model {
    Model nest(Symbol lhs, Model result);

    Model accept(int ch);

	Model set(Symbol key, Model value);
	
	void addAlternative(Model result);
}
