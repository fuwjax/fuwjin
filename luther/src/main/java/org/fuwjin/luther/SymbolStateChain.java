package org.fuwjin.luther;

/**
 * Created by fuwjax on 1/14/15.
 */
public interface SymbolStateChain {
    SymbolStateChain ensure(String name, SymbolBuilder symbol);

    SymbolStateChain ensure(String name, Codepoints step);

    void complete(String name);
}
