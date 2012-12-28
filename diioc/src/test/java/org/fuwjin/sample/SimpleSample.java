package org.fuwjin.sample;

public class SimpleSample {
	private String message;
	
	public SimpleSample(){
		this("simple sample");
	}

	public SimpleSample(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
