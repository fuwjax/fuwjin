package org.fuwjin.luther.bnf;

import org.fuwjin.luther.bnf.BnfGrammar.Builder;
import org.fuwjin.luther.builder.Codepoints;

public class CharClass implements Expression{
	private Codepoints codepoints;

	public CharClass(Codepoints codepoints) {
		this.codepoints = codepoints;
	}

	@Override
	public Object toStep(Builder builder) {
		return codepoints;
	}
}
