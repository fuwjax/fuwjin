package org.fuwjin.luther;

import java.util.Objects;
import java.util.Set;


public class Transition{
	private SymbolState state;
	private ParsePosition orig;
	private Model result;
	
	public Transition(Symbol s, ParsePosition orig){
		this(s.start(), orig, s.model());
	}

	private Transition(SymbolState state, ParsePosition orig, Model result) {
		this.state = state;
		this.orig = orig;
		this.result = result;
   }

	public Transition accept(Symbol trans, Model child){
		SymbolState to = state.accept(trans);
		return to == null ? null : new Transition(to, orig, result.set(trans, child));
	}

	public Transition accept(int a) {
		SymbolState to = state.accept(a);
		return to == null ? null : new Transition(to, orig, result.accept(a));
   }
	
	public Set<Symbol> predict(){
		return state.predict();
	}

	public boolean isComplete() {
	   return state.isComplete();
   }

	public Symbol lhs() {
	   return state.lhs();
   }

	public ParsePosition origin() {
		return orig;
	}
	
	public Set<Symbol> pending(){
		return state.pending();
	}

	public Symbol rightCycle() {
	   return state.rightCycle();
   }
	
	@Override
	public String toString() {
	   return "["+state+", "+orig.index+"]"+result;
	}

	public Model result() {
		return result;
   }
	
	@Override
	public boolean equals(Object obj) {
		try{
			Transition o = (Transition)obj;
			return state.equals(o.state) && orig.equals(o.orig); 
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public int hashCode() {
	   return Objects.hash(state, orig);
	}

	public Transition mark(Transition cause) {
	   return new Transition(state, orig, result.nest(cause.lhs(), cause.result));
   }
}