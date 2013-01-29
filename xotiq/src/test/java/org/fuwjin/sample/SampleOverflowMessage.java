package org.fuwjin.sample;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.xotiq.impl.BaseMessage;
import org.fuwjin.xotiq.impl.OverflowMessage;
import org.fuwjin.xotiq.impl.PropertyActor;

public class SampleOverflowMessage extends BaseMessage<SampleOverflowSchema> implements OverflowMessage {
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
		public String convert(Object value) {
			return String.valueOf(value);
		}
	};
	public static final SampleOverflowSchema OVER_SCHEMA = new SampleOverflowSchema();
	
	private boolean hasName;
	private String $name;
	private Map<Object,Object> overflow;

	public SampleOverflowMessage() {
		super(OVER_SCHEMA);
	}
	
	protected SampleOverflowMessage(SampleOverflowSchema schema){
		super(schema);
	}
	
	public String getName() {
		return schema().name.get(this);
	}

	public String setName(String name) {
		return schema().name.set(this, name);
	}

	@Override
	public Map<Object, Object> overflow() {
		if(overflow == null){
			overflow = new HashMap<Object,Object>();
		}
		return overflow;
	}
}
