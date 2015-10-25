package org.fuwjin.luther;

import java.io.IOException;

import org.echovantage.util.io.IntReader;

public interface Grammar {
	Object parse(IntReader reader) throws IOException;
}
