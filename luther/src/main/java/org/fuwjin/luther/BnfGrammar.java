package org.fuwjin.luther;

import org.echovantage.util.io.IntReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.function.IntPredicate;

public class BnfGrammar extends GrammarBuilder {
	private final Grammar bnf;

	private static Codepoints of(int option){
		return new Codepoints().add(option);
	}

	private static Codepoints any(int... options){
		return new Codepoints().add(options);
	}

	public BnfGrammar() {
	   rule(false, "<ignore>", any(' ', '\n', '\t', '\r'));
	   rule(true, "<start>", symbol("rules"), symbol("<ignore>"));
	   rule(true, "rules", symbol("directive"), symbol("rules"));
	   rule(true, "rules", symbol("rule"), symbol("rules"));
	   rule(true, "rules");
	   rule(true, "directive", '#', symbol("symbol"), symbol("expression"));
	   rule(true, "rule", symbol("symbol"), ':', symbol("expression"));
	   rule(true, "expression", symbol("symbol"), symbol("expression"));
	   rule(true, "expression", symbol("literal"), symbol("expression"));
	   rule(true, "expression", symbol("class"), symbol("expression"));
	   rule(true, "expression");
	   
	   rule(false, "symbol", of('_').range('A','Z').range('a','z'), symbol("symboltail"));
	   rule(false, "symboltail", of('_').range('A','Z').range('a', 'z').range('0','9'), symbol("symboltail"));
	   rule(false, "symboltail");
	   rule(false, "literal", '\'', symbol("single"), '\'');
	   rule(false, "single", any('\'','\\').negate(), symbol("single"));
	   rule(false, "single", symbol("escape"), symbol("single"));
	   rule(false, "single");
	   rule(false, "escape", '\\', any('\\', '"', '\'', 'n', 't', 'r').negate());
	   rule(false, "class", '[', symbol("chars"), ']');
	   rule(false, "class", '[', '^', symbol("chars"), ']');
	   rule(false, "chars", symbol("char"), symbol("chars"));
	   rule(false, "chars", symbol("range"), symbol("chars"));
	   rule(false, "chars");
	   rule(false, "char", any('\\', ']', '-').negate());
	   rule(false, "char", symbol("escape"));
	   rule(false, "range", symbol("char"), '-', symbol("char"));
		bnf = build("<start>");
   }
	
	public Grammar grammar(IntReader input) throws IOException {
	   Model model = bnf.parse(input);
	   GrammarBuilder g = new GrammarBuilder();
	   processRules(g, model);
	   return g.build("<start>");
	}
	
	private void processRules(GrammarBuilder g, Model model) {
		assert "rules".equals(name(model));
		Model directive = child(model, "directive");
		if(directive != null){
			processDirective(g, directive);
		}
		Model rule = child(model, "rule");
		if(rule != null){
			processRule(g, rule);
		}
		Model rules = child(model, "rules");
		if(rules != null){
			processRules(g, rules);
		}
   }

	private static StandardModel child(StandardModel m, String name) {
		for(StandardModel child: m.children()){
			if(name.equals(name(child))){
				return child;
			}
		}
	   return null;
   }

	private void processRule(GrammarBuilder g, Model model) {
		assert "rule".equals(name(model));
		String symbol = child(model, "symbol").match();
		Model expression = child(model, "expression");
		processExpression(g.symbol(symbol).start(), expression);
   }

	private void processExpression(SymbolStateChain state, Model expression) {
	   
   }

	private void processDirective(GrammarBuilder g, Model model) {
		assert "directive".equals(name(model));
		String symbol = child(model, "symbol").match();
		Model expression = child(model, "expression");
		switch(symbol){
			case "start":
				processExpression(g.symbol("<start>").start(), expression);
				break;
			case "ignore":
				SymbolBuilder ignore = g.symbol("<ignore>");
				ignore.start()
						.complete(".");
				ignore.start()
						.ensure(".<ignore> <ignore>", ignore)
						.ensure("<ignore>.<ignore>", ignore)
						.complete("<ignore> <ignore>.");
				processExpression(ignore.start(), expression);
		}
   }

	static String name(Model m) {
		return m.symbol() == null ? null : m.symbol().name();
	}
	
	private SymbolBuilder rule(boolean useIgnore, String lhs, Object... steps) {
		SymbolBuilder s = symbol(lhs);
		SymbolStateChain state = s.start();
	   String[] names = names(steps);
	   int index = 0;
	   for(Object step: steps){
	   	if(useIgnore && index > 0){
	   		state = state.ensure(name(index, names), symbol("<ignore>"));
	   	}
	   	if(step instanceof SymbolBuilder){
	   		state = state.ensure(name(index, names), (SymbolBuilder)step);
	   	}else if(step instanceof Codepoints){
	   		state = state.ensure(name(index, names), (Codepoints)step);
	   	}
	   	++index;
	   }
	   state.complete(name(index, names));
	   return s;
   }
	
	private static String[] names(Object... steps){
		String[] names = new String[steps.length];
		for(int i=0;i<names.length;i++){
	   	if(steps[i] instanceof Symbol){
	   		names[i] = ((Symbol)steps[i]).name();
	   	}else if(steps[i] instanceof Codepoints){
	   		names[i] = steps[i].toString();
	   	}
		}
		return names;
	}

	private static String name(int dot, String... names){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<names.length;i++){
			builder.append(i == dot ? '.' : i>0 ? ' ' : "").append(names[i]);
		}
		if(dot >= names.length){
			builder.append('.');
		}
		return builder.toString();
	}
}
