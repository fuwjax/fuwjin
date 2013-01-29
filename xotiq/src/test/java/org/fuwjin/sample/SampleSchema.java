package org.fuwjin.sample;

import org.fuwjin.xotiq.Schema;
import org.fuwjin.xotiq.impl.BaseProperty;
import org.fuwjin.xotiq.impl.MessageSchema;

public class SampleSchema extends MessageSchema<SampleMessage> {
	public final BaseProperty<SampleMessage, String> name=createProperty("name", SampleMessage.name);
	
	public SampleSchema() {
		// no parent
	}
	
	public SampleSchema(Schema parent){
		super(parent);
	}
	
	@Override
	public Class<SampleMessage> type() {
		return SampleMessage.class;
	}
	
	@Override
	public SampleMessage create() {
		return new SampleMessage(this);
	}
}
