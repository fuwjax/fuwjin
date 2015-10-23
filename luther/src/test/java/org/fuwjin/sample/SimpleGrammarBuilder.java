package org.fuwjin.sample;

import java.util.function.Function;

import org.fuwjin.luther.builder.Codepoints;
import org.fuwjin.luther.builder.GrammarBuilder;
import org.fuwjin.luther.builder.SymbolBuilder;
import org.fuwjin.luther.builder.SymbolStateBuilder;

public class SimpleGrammarBuilder extends GrammarBuilder {
	public SimpleGrammarBuilder rule(String lhs, String rhs){
		SymbolBuilder s = symbol(lhs);
		SymbolStateBuilder state = s.start();
		for(int i=0;i<rhs.length();i++){
			char c = rhs.charAt(i);
			if(Character.isUpperCase(c)){
				state = state.ensure(rhs.substring(0,i)+"."+rhs.substring(i), symbol(Character.toString(c)));
			}else{
				state = state.ensure(rhs.substring(0,i)+"."+rhs.substring(i), new Codepoints().add(c));
			}
		}
		state.complete(rhs+".", Function.identity());
		return this;
	}
}
