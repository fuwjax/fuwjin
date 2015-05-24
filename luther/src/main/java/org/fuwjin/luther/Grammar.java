package org.fuwjin.luther;

import org.echovantage.util.io.IntReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;

public class Grammar {
    private final Symbol accept;

    public Grammar(Symbol accept){
        this.accept = accept;
    }

    public Model parse(IntReader input) throws IOException {
        ParsePosition set = new ParsePosition(accept);
        OfInt iter = input.stream().iterator();
        while (iter.hasNext()) {
            set = set.accept(iter.nextInt());
        }
        return set.result(accept);
    }

    @Override
    public String toString() {
        return accept.toString();
    }
}