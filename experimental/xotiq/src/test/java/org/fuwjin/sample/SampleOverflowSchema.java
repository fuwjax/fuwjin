package org.fuwjin.sample;

import static org.fuwjin.xotiq.impl.PrimitiveSchema.OBJECT;

import java.util.Map;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;
import org.fuwjin.xotiq.impl.BaseMessageSchema;
import org.fuwjin.xotiq.impl.OverflowPropertyActor;

public class SampleOverflowSchema extends BaseMessageSchema<SampleOverflowMessage> {
	public final Property<SampleOverflowMessage, String> name=addProperty("name", SampleOverflowMessage.name);
	
	@Override
	public SampleOverflowMessage create() {
		return new SampleOverflowMessage();
	}
	
	@Override
	public SampleOverflowMessage create(Message<?> parent) {
		return new SampleOverflowMessage(parent);
	}
	
	@Override
	public boolean isMessage(Object message) {
		return message instanceof SampleOverflowMessage;
	}

	@Override
	public SampleOverflowMessage convert(Object value) {
		return (SampleOverflowMessage)value;
	}
	
	@Override
	protected <V> Property<SampleOverflowMessage, V> unknownProperty(Object key, Schema<V> type) {
		return addProperty(key, new OverflowPropertyActor<SampleOverflowMessage,Object,Object>(key, OBJECT){
			@Override
			protected Map<Object, Object> overflow(SampleOverflowMessage message) {
				return message.overflow();
			}
		}).as(type);
	}
}
