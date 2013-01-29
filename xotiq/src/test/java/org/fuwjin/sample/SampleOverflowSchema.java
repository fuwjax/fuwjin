package org.fuwjin.sample;

import org.fuwjin.xotiq.Schema;
import org.fuwjin.xotiq.impl.BaseProperty;
import org.fuwjin.xotiq.impl.OverflowSchema;

public class SampleOverflowSchema extends OverflowSchema<SampleOverflowMessage> {
	public final BaseProperty<SampleOverflowMessage, String> name=createProperty("name", SampleOverflowMessage.name);
	
	public SampleOverflowSchema() {
		// no parent
	}
	
	public SampleOverflowSchema(Schema parent){
		super(parent);
	}
	
	@Override
	public Class<SampleOverflowMessage> type() {
		return SampleOverflowMessage.class;
	}
	
	@Override
	public SampleOverflowMessage create() {
		return new SampleOverflowMessage(this);
	}
}
