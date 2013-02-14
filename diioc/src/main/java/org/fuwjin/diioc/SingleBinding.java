package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.EXISTS;

import javax.inject.Provider;

public class SingleBinding<T> implements Binding<T> {
	private Binding<T> binding;
	private AbstractContext root;
	private Provider<T> provider;

	public SingleBinding(Binding<T> binding, AbstractContext root) {
		this.binding = binding;
		this.root = root;
	}

	@Override
	public Provider<T> provider(AbstractContext ignore) {
		if(provider == null){
			provider = new Provider<T>(){
				private T instance;
				@Override
				public T get() {
					if(instance == null){
						try{
							instance = binding.provider(root).get();
						}catch(Exception e){
							throw new ProviderException("Could not create singleton instance", e);
						}
					}
					return instance;
				}
			};
		}
		return provider;
	}

	@Override
	public Key<T> key() {
		return binding.key();
	}
	
	@Override
	public Strategy strategy() {
		return EXISTS;
	}
}
