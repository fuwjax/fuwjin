package org.fuwjin.diioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingMap {
	private Map<Class<?>, List<Binding<?>>> bindings = new HashMap<Class<?>, List<Binding<?>>>();

	public void add(Binding<?> binding){
		List<Binding<?>> list = bindings.get(binding.key().type());
		if(list == null){
			list = new ArrayList<Binding<?>>();
			bindings.put(binding.key().type(), list);
		}
		list.add(binding);
	}
	
	public <T> Binding<T> bind(Key<T> key) throws Exception {
		Binding<?> best = null;
		for(Map.Entry<Class<?>, List<Binding<?>>> entry: bindings.entrySet()){
			if(key.type().isAssignableFrom(entry.getKey())){
				best = betterOf(key, entry.getValue(), best);
			}
		}
		return (Binding<T>)best;
	}

	protected <T> Binding<?> betterOf(Key<T> key, List<Binding<?>> targets, Binding<?> best) {
		for(Binding<?> target: targets){
			best = key.betterOf(target, best);
		}
		return best;
	}

}
