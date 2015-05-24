package org.fuwjin.sample;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Schema;
import org.fuwjin.xotiq.impl.BaseMessage;
import org.fuwjin.xotiq.impl.PrimitiveSchema;
import org.fuwjin.xotiq.impl.PropertyActor;

public class SampleMessage extends BaseMessage<SampleMessage> {
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
		public Schema<String> type() {
			return PrimitiveSchema.STRING;
		}
	};
	public static final SampleSchema SCHEMA = new SampleSchema();
	
	private boolean hasName;
	private String $name;

	public SampleMessage() {
	}
	
	protected SampleMessage(Message<?> parent){
		super(parent);
	}
	
	@Override
	public SampleSchema schema() {
		return SCHEMA;
	}
	
	public SampleMessage withName(String name){
		setName(name);
		return this;
	}
	
	public String getName() {
		return schema().name.get(this);
	}

	public String setName(String name) {
		return schema().name.set(this, name);
	}
	
	public boolean hasName(){
		return schema().name.has(this);
	}
	
	public String clearName(){
		return schema().name.clear(this);
	}
}
