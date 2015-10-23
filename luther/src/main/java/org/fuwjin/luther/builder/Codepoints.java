package org.fuwjin.luther.builder;

import java.util.BitSet;
import java.util.function.IntPredicate;

/**
 * Created by fuwjax on 1/12/15.
 */
public class Codepoints {
    private static final BitSet NOTHING = new BitSet();
    private final BitSet bits;
    private final boolean negate;

    public Codepoints(){
        this(NOTHING, false);
    }

    private Codepoints(BitSet bits, boolean negate){
        this.bits = bits;
        this.negate = negate;
    }

    public Codepoints add(int... codepoints) {
        BitSet set = (BitSet)bits.clone();
        for(int codepoint: codepoints){
            set.set(codepoint, !negate);
        }
        return new Codepoints(set, negate);
    }

    public Codepoints negate() {
        return new Codepoints(bits, !negate);
    }

    public Codepoints range(int start, int end) {
        BitSet set = (BitSet)bits.clone();
        set.set(start, end+1, !negate);
        return new Codepoints(set, negate);
    }

    @Override
    public int hashCode() {
        return negate ? ~bits.hashCode() : bits.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        try{
            Codepoints o = (Codepoints)obj;
            return getClass().equals(o.getClass()) && bits.equals(o.bits) && negate == o.negate;
        }catch(Exception e){
            return false;
        }
    }

    public IntPredicate toPredicate() {
        return bits::get;
    }
}
