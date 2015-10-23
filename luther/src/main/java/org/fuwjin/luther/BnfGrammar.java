package org.fuwjin.luther;

import java.io.IOException;

import org.echovantage.util.io.IntReader;

public class BnfGrammar extends GrammarBuilder {
	private final Grammar bnf;

	private static Codepoints of(final int option) {
		return new Codepoints().add(option);
	}

	private static Codepoints any(final int... options) {
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

		rule(false, "symbol", of('_').range('A', 'Z').range('a', 'z'), symbol("symboltail"));
		rule(false, "symboltail", of('_').range('A', 'Z').range('a', 'z').range('0', '9'), symbol("symboltail"));
		rule(false, "symboltail");
		rule(false, "literal", '\'', symbol("single"), '\'');
		rule(false, "single", any('\'', '\\').negate(), symbol("single"));
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

	public Grammar grammar(final IntReader input) throws IOException {
		final Model model = bnf.parse(input);
		final GrammarBuilder g = new GrammarBuilder();
		processRules(g, model);
		return g.build("<start>");
	}

	private void processRules(final GrammarBuilder g, final Model model) {
		assert "rules".equals(name(model));
		final Model directive = (Model) child(model, "directive");
		if (directive != null) {
			processDirective(g, directive);
		}
		final Model rule = (Model) child(model, "rule");
		if (rule != null) {
			processRule(g, rule);
		}
		final Model rules = (Model) child(model, "rules");
		if (rules != null) {
			processRules(g, rules);
		}
	}

	private static Node child(final Model m, final String name) {
		for (final Node child : m.children()) {
			if (name.equals(name(child))) {
				return child;
			}
		}
		return null;
	}

	private void processRule(final GrammarBuilder g, final Model model) {
		assert "rule".equals(name(model));
		final String symbol = child(model, "symbol").match();
		final Model expression = (Model) child(model, "expression");
		processExpression(g.symbol(symbol).start(), expression);
	}

	private void processExpression(final SymbolStateChain state, final Model expression) {

	}

	private void processDirective(final GrammarBuilder g, final Model model) {
		assert "directive".equals(name(model));
		final String symbol = child(model, "symbol").match();
		final Model expression = (Model) child(model, "expression");
		switch (symbol) {
		case "start":
			processExpression(g.symbol("<start>").start(), expression);
			break;
		case "ignore":
			final SymbolBuilder ignore = g.symbol("<ignore>");
			ignore.start().complete(".");
			ignore.start().ensure(".<ignore> <ignore>", ignore).ensure("<ignore>.<ignore>", ignore)
					.complete("<ignore> <ignore>.");
			processExpression(ignore.start(), expression);
		}
	}

	static String name(final Node m) {
		return m instanceof Model ? ((Model) m).symbol().name() : null;
	}

	private SymbolBuilder rule(final boolean useIgnore, final String lhs, final Object... steps) {
		final SymbolBuilder s = symbol(lhs);
		SymbolStateChain state = s.start();
		final String[] names = names(steps);
		int index = 0;
		for (final Object step : steps) {
			if (useIgnore && index > 0) {
				state = state.ensure(name(index, names), symbol("<ignore>"));
			}
			if (step instanceof SymbolBuilder) {
				state = state.ensure(name(index, names), (SymbolBuilder) step);
			} else if (step instanceof Codepoints) {
				state = state.ensure(name(index, names), (Codepoints) step);
			}
			++index;
		}
		state.complete(name(index, names));
		return s;
	}

	private static String[] names(final Object... steps) {
		final String[] names = new String[steps.length];
		for (int i = 0; i < names.length; i++) {
			if (steps[i] instanceof Symbol) {
				names[i] = ((Symbol) steps[i]).name();
			} else if (steps[i] instanceof Codepoints) {
				names[i] = steps[i].toString();
			}
		}
		return names;
	}

	private static String name(final int dot, final String... names) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			builder.append(i == dot ? '.' : i > 0 ? ' ' : "").append(names[i]);
		}
		if (dot >= names.length) {
			builder.append('.');
		}
		return builder.toString();
	}
}
