package org.fuwjin.sample;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.impl.BaseMessageSchema;

public class SampleSchema extends BaseMessageSchema<SampleMessage> {
	public final Property<SampleMessage, String> name=addProperty("name", SampleMessage.name);
	
	@Override
	public SampleMessage create() {
		return new SampleMessage();
	}
	
	public SampleMessage create(Message<?> parent) {
		return new SampleMessage(parent);
	}

	@Override
	public SampleMessage convert(Object value) {
		return (SampleMessage)value;
	}
	
	@Override
	public boolean isMessage(Object message) {
		return message instanceof SampleMessage;
	}
}
