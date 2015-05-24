package org.fuwjin.luther;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParsePosition {
	private Map<Transition, Transition> items = new HashMap<>();
	private Map<Symbol, Transition> transitives = new HashMap<>();
	private Set<Symbol> predict = new LinkedHashSet<>();
	public final int index;
	
	private ParsePosition(int index){
		this.index = index;
	}
	
	public ParsePosition(Symbol accept) {
		index = 0;
		add(new Transition(accept, this));
		predict();
   }

	private void add(Transition next){
		if(next == null){
			return;
		}
		if(!items.containsKey(next)){
			items.put(next, next);
			if(next.isComplete()){
				Model result = next.result();
				Transition mark = markOf(next);
				if(mark != null){
					add(mark.accept(next.lhs(), result));
				}else{
					for(Transition item: next.origin().awaiting(next.lhs())){
						add(item.accept(next.lhs(), result));
					}
				}
			}
			predict.addAll(next.predict());
		}else{ // grammar is ambiguous
			items.get(next).result().addAlternative(next.result());
		}
	}
	
	public ParsePosition accept(int ch) {
		ParsePosition set = new ParsePosition(index+1);
		for(Transition item: items.values()){
			set.add(item.accept(ch));
		}
		set.predict();
      return set;
   }

	private void predict() {
	   for(Symbol s: predict){
	   	Transition t = new Transition(s, this);
			items.put(t,t);
		}
		for(Transition item: items.values()){
			Symbol rr = item.rightCycle();
			if(rr != null && awaiting(rr).size() == 1){
				Transition mark = markOf(item);
				if(mark == null){
					transitives.put(rr, item);
				}else{
					transitives.put(rr, mark.mark(item));
				}
			}
		}
   }
	
	private static Transition markOf(Transition item){
		return item.origin().transitives.get(item.lhs());
	}
	
	private Collection<Transition> awaiting(Symbol transition) {
		// this could be more efficient
		List<Transition> list = new ArrayList<>();
		for(Transition item: items.values()){
			if(item.pending().contains(transition)){
				list.add(item);
			}
		}
	   return list;
   }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(index).append("\n");
		for(Transition item: items.values()){
			builder.append(item).append("\n");
		}
		for(Map.Entry<Symbol, Transition> entry: transitives.entrySet()){
			builder.append(entry.getKey().name()).append(": ").append(entry.getValue()).append("\n");
		}
	   return builder.toString();
	}

    public Model result(Symbol symbol) {
        for(Transition item: items.values()){
            if(symbol.equals(item.lhs()) && item.isComplete()){
                return item.result();
            }
        }
        throw new IllegalArgumentException("Invalid or incomplete input");
    }
}