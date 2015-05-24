package org.fuwjin.luther;

import java.util.Objects;
import java.util.function.Supplier;

public class Symbol {
    private String name;
    private SymbolState start;
    private String toString;
    private Supplier<Model> modelSupplier;

    void init(String name, SymbolState start, String toString, Supplier<Model> modelSupplier) {
        this.name = name;
        this.start = start;
        this.toString = toString;
        this.modelSupplier = modelSupplier;
    }

    public SymbolState start() {
        return start;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Symbol o = (Symbol) obj;
            return Objects.equals(name, o.name);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return toString;
    }

    public String name() {
        return name;
    }

    public Model model() {
        return modelSupplier.get();
    }
}