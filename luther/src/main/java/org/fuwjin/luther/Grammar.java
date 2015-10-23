package org.fuwjin.luther;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import org.echovantage.util.io.IntReader;
import org.fuwjin.luther.builder.SymbolBuilder;
import org.fuwjin.luther.model.Model;
import org.fuwjin.luther.model.Node;
import org.fuwjin.luther.model.Value;

public class Grammar {
	private final Symbol accept;

	public Grammar(final Symbol accept) {
		this.accept = accept;
	}

	public Grammar(SymbolBuilder start) {
        SymbolBuilder accept = new SymbolBuilder("ACCEPT");
        accept.start().ensure("." + start.name(), start).complete(start.name() + ".", Function.identity());
        accept.checkNullable();
        accept.collapse();
        accept.buildPredict();
        accept.checkRightRoot();
        this.accept = accept.build();
	}

	public Object parse(final IntReader input) throws IOException {
		Model model = (Model)new ParseState().parse(accept, input);
		List<Node> children = model.children();
		if(children.isEmpty()){
			return null;
		}
		Node result = children.get(0);
		return result instanceof Model ? ((Model)result).value() : result;
	}

	@Override
	public String toString() {
		return accept.toString();
	}
}