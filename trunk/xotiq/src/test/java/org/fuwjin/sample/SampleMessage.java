package org.fuwjin.sample;

import org.fuwjin.xotiq.impl.BaseMessage;
import org.fuwjin.xotiq.impl.PropertyActor;

public class SampleMessage extends BaseMessage<SampleSchema> {
	public static final PropertyActor<SampleMessage, String> name = new PropertyActor<SampleMessage, String>(){
		@Override
		public String get(SampleMessage message) {
			return message.$name;
		}

		@Override
		public void set(SampleMessage message, String value) {
			message.$name = value;
			message.hasName = true;
		}

		@Override
		public void clear(SampleMessage message) {
			message.hasName = false;
		}

		@Override
		public boolean has(SampleMessage message) {
			return message.hasName;
		}
		
		@Override
		public String convert(Object value) {
			return String.valueOf(value);
		}
	};
	public static final SampleSchema SCHEMA = new SampleSchema();
	
	private boolean hasName;
	private String $name;

	public SampleMessage() {
		super(SCHEMA);
	}
	
	protected SampleMessage(SampleSchema schema){
		super(schema);
	}
	
	public String getName() {
		return schema().name.get(this);
	}

	public String setName(String name) {
		return schema().name.set(this, name);
	}
}
