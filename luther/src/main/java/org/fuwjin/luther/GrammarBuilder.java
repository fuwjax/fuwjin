package org.fuwjin.luther;

import org.echovantage.util.io.IntReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrammarBuilder {
    private Map<String, SymbolBuilder> symbols = new HashMap<>();

    public SymbolBuilder symbol(String name) {
        SymbolBuilder symbol = symbols.get(name);
        if (symbol == null) {
            symbol = new SymbolBuilder(name);
            symbols.put(name, symbol);
        }
        return symbol;
    }

    public Grammar build(String start) {
        for (SymbolBuilder s : symbols.values()) {
            s.checkNullable();
        }
        for (SymbolBuilder s : symbols.values()) {
            s.collapse();
        }
        for (SymbolBuilder s : symbols.values()) {
            s.buildPredict();
        }
        for (SymbolBuilder s : symbols.values()) {
            s.checkRightCycle();
        }
        for (SymbolBuilder s : symbols.values()) {
            s.checkRightRoot();
        }
        SymbolBuilder accept = new SymbolBuilder("ACCEPT");
        accept.start().ensure("." + start, symbol(start)).complete(start + ".");
        accept.checkNullable();
        accept.collapse();
        accept.buildPredict();
        accept.checkRightRoot();
        return new Grammar(accept.build());
    }
}