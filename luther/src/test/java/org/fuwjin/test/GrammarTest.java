package org.fuwjin.test;

import org.fuwjin.luther.CharModel;
import org.fuwjin.luther.Grammar;
import org.fuwjin.luther.Model;
import org.fuwjin.luther.StandardModel;
import org.fuwjin.sample.SimpleGrammar;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.echovantage.util.Assert2.assertThrown;
import static org.echovantage.util.io.IntReader.codepoints;
import static org.junit.Assert.*;

public class GrammarTest {
    private SimpleGrammar g;
    private Grammar grammar;

    @Before
    public void setup() {
        g = new SimpleGrammar();
    }

    @Test
    public void testSimple() throws Exception {
        g.rule("S", "a");
        grammar = g.build("S");
        assertEquals("a", parse("a").match());
        assertThrown(Exception.class, () -> parse("b"));
    }

    @Test
    public void testSymbol() throws Exception {
        g.rule("S", "a");
        g.rule("C", "b");
        g.rule("S", "C");
        grammar = g.build("S");
        assertEquals("a", parse("a").match());
        assertEquals("b", parse("b").match());
        assertThrown(Exception.class, () -> parse("c"));
    }

    private Model n(String id, Model... children) {
        return new StandardModel(g.symbol(id).build(), children);
    }

    private Model t(int ch) {
        return new CharModel(ch);
    }

    @Test
    public void testAmbiguous() throws Exception {
        g.rule("S", "sS");
        g.rule("S", "IS");
        g.rule("S", "I");
        g.rule("S", "s");
        g.rule("I", "iSE");
        g.rule("I", "iS");
        g.rule("E", "eS");
        grammar = g.build("S");
        assertEquals(n("S", t('s')), parse("s"));
        assertEquals(n("S", n("I", t('i'), n("S", t('s')))), parse("is"));
        assertEquals(n("S", n("I", t('i'), n("S", t('s')), n("E", t('e'), n("S", t('s'))))), parse("ises"));
        assertEquals(n("S", n("I", t('i'), n("S", n("I", t('i'), n("S", t('s')))))), parse("iis"));
        assertEquals(n("S", n("I", t('i'), n("S", n("I", t('i'), n("S", t('s')), n("E", t('e'), n("S", t('s'))))))), parse("iises"));
    }

    @Test
    public void testRightRecurse() throws Exception {
        g.rule("S", "aS");
        g.rule("C", "");
        g.rule("S", "C");
        g.rule("C", "aCb");
        grammar = g.build("S");
        assertEquals(n("S", t('a'), n("S", t('a'), n("S", t('a'), n("S", t('a'))))), parse("aaaa"));
        assertEquals(n("S", t('a')), parse("a"));
        assertEquals(n("S", n("C", t('a'), t('b'))), parse("ab"));
        assertEquals(n("S", n("C", t('a'), n("C", t('a'), t('b')), t('b'))), parse("aabb"));
        assertEquals(n("S", t('a'), n("S", n("C", t('a'), n("C", t('a'), t('b')), t('b')))), parse("aaabb"));
        assertNull(parse(""));
        assertThrown(Exception.class, () -> parse("abb"));
    }

    @Test
    public void testSomethingNatural() throws Exception {
        g.rule("S", "SL");
        g.rule("S", "L");
        g.rule("L", "Xn");
        g.rule("X", "Xx");
        g.rule("X", "x");
        grammar = g.build("S");
        assertEquals(n("S", n("S", n("S", n("L", n("X", n("X", n("X", t('x')), t('x')), t('x')), t('n'))), n("L", n("X", t('x')), t('n'))), n("L", n("X", n("X", t('x')), t('x')), t('n'))), parse("xxxnxnxxn"));
        assertEquals(n("S", n("L", n("X", t('x')), t('n'))), parse("xn"));
        assertEquals(n("S", n("S", n("S", n("S", n("L", n("X", t('x')), t('n'))), n("L", n("X", t('x')), t('n'))), n("L", n("X", t('x')), t('n'))), n("L", n("X", t('x')), t('n'))), parse("xnxnxnxn"));
        assertThrown(Exception.class, () -> parse(""));
        assertThrown(Exception.class, () -> parse("xxnxx"));
        assertThrown(Exception.class, () -> parse("nxxxn"));
        assertThrown(Exception.class, () -> "xxnnxxn");
    }

    private Model parse(String input) throws IOException {
        return grammar.parse(codepoints(input));
    }
}
