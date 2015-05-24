package org.fuwjin.sample;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Schema;
import org.fuwjin.xotiq.impl.BaseMessage;
import org.fuwjin.xotiq.impl.PrimitiveSchema;
import org.fuwjin.xotiq.impl.PropertyActor;

public class SampleOverflowMessage extends BaseMessage<SampleOverflowMessage> {
	public static final PropertyActor<SampleOverflowMessage, String> name = new PropertyActor<SampleOverflowMessage, String>(){
		@Override
		public String get(SampleOverflowMessage message) {
			return message.$name;
		}

		@Override
		public void set(SampleOverflowMessage message, String value) {
			message.$name = value;
			message.hasName = true;
		}

		@Override
		public void clear(SampleOverflowMessage message) {
			message.hasName = false;
		}

		@Override
		public boolean has(SampleOverflowMessage message) {
			return message.hasName;
		}
		
		@Override
		public Schema<String> type() {
			return PrimitiveSchema.STRING;
		}
	};
	public static final SampleOverflowSchema SCHEMA = new SampleOverflowSchema();
	
	private boolean hasName;
	private String $name;
	private Map<Object,Object> overflow;

	public SampleOverflowMessage() {
	}
	
	protected SampleOverflowMessage(Message<?> parent){
		super(parent);
	}
	
	@Override
	public SampleOverflowSchema schema() {
		return SCHEMA;
	}
	
	protected Map<Object, Object> overflow() {
		if(overflow == null){
			overflow = new HashMap<Object,Object>();
		}
		return overflow;
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

