package org.fuwjin.luther.builder;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.luther.Grammar;

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
        return new Grammar(symbol(start));
    }
}