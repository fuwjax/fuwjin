package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class MessageWrapperSchema<M> implements Schema<MessageWrapper<M>> {
	private Schema<M> schema;

	public MessageWrapperSchema(Schema<M> schema){
		this.schema = schema;
	}
	
	@Override
	public MessageWrapper<M> create() {
		return new MessageWrapper<M>(this, schema.create());
	}
	
	@Override
	public MessageWrapper<M> create(Message<?> parent) {
		return new MessageWrapper<M>(this, schema.create(), parent);
	}

	@Override
	public MessageWrapper<M> convert(Object value) {
		return new MessageWrapper<M>(this, schema.convert(value));
	}
	
	protected Schema<M> wrapped(){
		return schema;
	}
	
	@Override
	public boolean isMessage(Object message) {
		return message instanceof MessageWrapper && wrapped().isMessage(((MessageWrapper<?>)message).wrapped());
	}
	
	@Override
	public <V> Property<MessageWrapper<M>, V> property(Object key, Schema<V> type) {
		return decorate(wrapped().property(key, type));
	}

	protected <V> Property<MessageWrapper<M>, V> decorate(Property<M,V> property) {
		return new MessageConversionProperty<MessageWrapper<M>,M,V>(this, property){
			@Override
			protected M convert(MessageWrapper<M> message) {
				return message.wrapped();
			}
		};
	}

	@Override
	public Message<?> parentOf(MessageWrapper<M> message) {
		return message.parent();
	}

	@Override
	public Property<MessageWrapper<M>, ?> property(Object key) {
		return decorate(wrapped().property(key));
	}

	@Override
	public Iterable<? extends Property<MessageWrapper<M>, ?>> properties() {
		return Iterables.transform(wrapped().properties(), new Function<Property<M,?>, Property<MessageWrapper<M>,?>>(){
			public Property<MessageWrapper<M>,?> apply(Property<M,?> property){
				return decorate(property);
			}
		});
	}

	@Override
	public Iterable<? extends Attribute<MessageWrapper<M>, ?>> attributes(final MessageWrapper<M> message) {
		return Iterables.transform(properties(), new Function<Property<MessageWrapper<M>,?>, Attribute<MessageWrapper<M>,?>>(){
			public Attribute<MessageWrapper<M>,?> apply(Property<MessageWrapper<M>,?> property){
				return property.attribute(message);
			}
		});
	}
}
