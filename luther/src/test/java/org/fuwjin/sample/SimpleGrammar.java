package org.fuwjin.sample;

import org.fuwjin.luther.*;

public class SimpleGrammar extends GrammarBuilder {
	public SimpleGrammar rule(String lhs, String rhs){
		SymbolBuilder s = symbol(lhs);
		SymbolStateChain state = s.start();
		for(int i=0;i<rhs.length();i++){
			char c = rhs.charAt(i);
			if(Character.isUpperCase(c)){
				state = state.ensure(rhs.substring(0,i)+"."+rhs.substring(i), symbol(Character.toString(c)));
			}else{
				state = state.ensure(rhs.substring(0,i)+"."+rhs.substring(i), new Codepoints().add(c));
			}
		}
		state.complete(rhs+".");
		return this;
	}
}
