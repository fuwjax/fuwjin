package org.fuwjin.luther.bnf;

import org.fuwjin.luther.bnf.BnfGrammar.Builder;

public interface Expression {
	Object toStep(Builder builder);
}
