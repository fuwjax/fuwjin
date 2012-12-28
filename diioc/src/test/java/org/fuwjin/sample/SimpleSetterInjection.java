package org.fuwjin.sample;

import javax.inject.Inject;

public class SimpleSetterInjection {
	private SimpleSample sample;
	
	@Inject
	public void setSimpleSample(SimpleSample sample){
		this.sample = sample;
	}
	
	@Override
	public String toString() {
		return "simple setter = "+sample;
	}

}
