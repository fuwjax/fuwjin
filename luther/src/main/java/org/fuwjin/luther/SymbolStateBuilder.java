package org.fuwjin.luther;

import java.util.*;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class SymbolStateBuilder implements SymbolStateChain {
	private SymbolBuilder lhs;
	private Set<String> names = new HashSet<>();
	private Map<SymbolBuilder, SymbolStateBuilder> symbolic = new HashMap<>();
	private Map<Codepoints, SymbolStateBuilder> literals = new HashMap<>();
	private Set<SymbolBuilder> predict;
	private SymbolBuilder rightCycle;
	private boolean complete;
	private SymbolState state;

	SymbolStateBuilder(SymbolBuilder lhs){
		this.lhs = lhs;
	}

	public SymbolStateBuilder ensure(String name, SymbolBuilder accept){
        names.add(name);
        return ensure(accept);
	}

    private SymbolStateBuilder ensure(SymbolBuilder accept) {
        SymbolStateBuilder s = symbolic.get(accept);
        if(s == null){
            s = new SymbolStateBuilder(lhs);
            symbolic.put(accept, s);
        }
        return s;
    }

    public SymbolStateBuilder ensure(String name, Codepoints literalMask){
        names.add(name);
        return ensure(literalMask);
	}

    private SymbolStateBuilder ensure(Codepoints literalMask) {
        SymbolStateBuilder s = literals.get(literalMask);
        if(s == null){
            // TODO: check for intersection
            s = new SymbolStateBuilder(lhs);
            literals.put(literalMask, s);
        }
        return s;
    }

    public void complete(String name) {
        names.add(name);
		this.complete = true;
   }

	public String walk(){
		StringBuilder builder = new StringBuilder();
		builder.append(this);
	   for(SymbolStateBuilder s: literals.values()){
	   	builder.append("\n").append(s.walk());
	   }
	   for(SymbolStateBuilder s: symbolic.values()){
	   	builder.append("\n").append(s.walk());
	   }
	   return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder().append(lhs.name());
		String d = " -> ";
		for(String name: names){
			builder.append(d).append(name);
			d = " | ";
		}
		return builder.toString();
	}

	private void merge(SymbolStateBuilder state) {
	   for(Map.Entry<Codepoints, SymbolStateBuilder> entry: state.literals.entrySet()){
	   	ensure(entry.getKey()).merge(entry.getValue());
	   }
	   for(Map.Entry<SymbolBuilder, SymbolStateBuilder> entry: state.symbolic.entrySet()){
	   	ensure(entry.getKey()).merge(entry.getValue());
	   }
	   names.addAll(state.names);
	   complete |= state.complete;
   }

	public boolean checkNullable() {
		if(complete){
			return true;
		}
		for(Map.Entry<SymbolBuilder, SymbolStateBuilder> entry: symbolic.entrySet()){
			if(entry.getValue().checkNullable() && entry.getKey().checkNullable()){
				return true;
			}
		}
		return false;
   }

	public void collapse() {
		for(SymbolStateBuilder s: literals.values()){
			s.collapse();
		}
        while(!Thread.currentThread().isInterrupted()) {
            try {
                for (Map.Entry<SymbolBuilder, SymbolStateBuilder> entry : symbolic.entrySet()) {
                    entry.getValue().collapse();
                    if (entry.getKey().isNullable()) {
                        merge(entry.getValue());
                    }
                }
                break;
            }catch(ConcurrentModificationException e){
                //continue;
            }
        }
	}

	public Set<SymbolBuilder> buildPredict(){
		if(predict != null){
			return predict;
		}
		if(symbolic.isEmpty()){
			predict = Collections.emptySet();
		}else{
			predict = new HashSet<>();
			for(SymbolBuilder s: symbolic.keySet()){
				if(predict.add(s) && !lhs.equals(s)){
					predict.addAll(s.buildPredict());
				}
			}
		}
		for(SymbolStateBuilder s: symbolic.values()){
			s.buildPredict();
		}
		for(SymbolStateBuilder s: literals.values()){
			s.buildPredict();
		}
		return predict;
   }

	public boolean checkRightCycle() {
		for(SymbolStateBuilder s: literals.values()){
			if(s.checkRightCycle()){
				return true;
			}
		}
		for(Map.Entry<SymbolBuilder, SymbolStateBuilder> entry: symbolic.entrySet()){
			if(entry.getValue().checkRightCycle()){
				return true;
			}
			if(entry.getValue().complete && entry.getKey().checkRightCycle()){
				return true;
			}
		}
		return false;
   }

	public void checkRightRoot() {
		for(SymbolStateBuilder s: literals.values()){
			s.checkRightRoot();
		}
		for(Map.Entry<SymbolBuilder, SymbolStateBuilder> entry: symbolic.entrySet()){
			entry.getValue().checkRightRoot();
		}
		if(symbolic.size() == 1 && literals.isEmpty()){
			for(Map.Entry<SymbolBuilder, SymbolStateBuilder> entry: symbolic.entrySet()){
				if(entry.getKey().isRightCycle() && entry.getValue().complete){
					rightCycle = entry.getKey();
				}
			}
		}
   }

	public SymbolState build() {
		if(state == null){
			state = new SymbolState();
            state.init(lhs.build(), symbolic.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().build(), e -> e.getValue().build())), literalFunction(), predict.stream().map(SymbolBuilder::build).collect(Collectors.toSet()), rightCycle == null ? null : rightCycle.build(), complete, toString());
		}
		return state;
	}

    private IntFunction<SymbolState> literalFunction() {
        Map<IntPredicate, SymbolState> states = literals.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toPredicate(), e -> e.getValue().build()));
        return ch -> {
            for(Map.Entry<IntPredicate, SymbolState> entry: states.entrySet()){
                if(entry.getKey().test(ch)){
                    return entry.getValue();
                }
            }
            return null;
        };
    }
}