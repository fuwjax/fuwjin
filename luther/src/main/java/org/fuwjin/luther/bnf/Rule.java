package org.fuwjin.luther.bnf;

import java.util.List;
import java.util.function.Function;

import org.fuwjin.luther.bnf.BnfGrammar.Builder;

public class Rule {
	private String lhs;
	private List<Expression> expression;

	public Rule(String lhs, List<Expression> expression) {
		this.lhs = lhs;
		this.expression = expression;
	}

	public void applyTo(Builder builder) {
		builder.rule(true, lhs, Function.identity(), expression.stream().map(x -> x.toStep(builder)).toArray());
	}

}
