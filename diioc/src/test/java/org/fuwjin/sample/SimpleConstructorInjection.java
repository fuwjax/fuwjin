package org.fuwjin.sample;

import javax.inject.Inject;

public class SimpleConstructorInjection {
	private SimpleSample sample;
	
	@Inject
	public SimpleConstructorInjection(SimpleSample sample){
		this.sample = sample;
	}
	
	@Override
	public String toString() {
		return "simple constructor = "+sample;
	}

}
