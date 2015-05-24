package org.fuwjin.xotiq.impl;

import static org.fuwjin.xotiq.impl.PrimitiveSchema.INTEGER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public class ListSchema<E> extends AbstractSchema<List<E>> {
	private Schema<Integer> keyType;
	private Schema<E> valueType;
	public ListSchema(Schema<E> type){
		this.keyType = INTEGER;
		this.valueType = type;
	}
	
	@Override
	public List<E> create() {
		return new ArrayList<E>();
	}
	
	@Override
	public List<E> convert(Object value) {
		return (List<E>)value;
	}
	
	@Override
	public boolean isMessage(Object message) {
		return message instanceof List;
	}

	@Override
	public Property<List<E>, E> property(Object key) {
		final Integer realKey = keyType.convert(key);
		return new BaseProperty<List<E>, E>(this, key, new PropertyActor<List<E>, E>(){
			@Override
			public E get(List<E> message) {
				if(has(message)){
					return message.get(realKey);
				}
				return null;
			}

			@Override
			public void set(List<E> message, E value) {
				if(realKey == message.size()){
					message.add(realKey, value);
				}else{
					message.set(realKey, value);
				}
			}

			@Override
			public boolean has(List<E> message) {
				return realKey >= 0 && realKey < message.size();
			}

			@Override
			public void clear(List<E> message) {
				if(has(message)){
					message.remove(realKey.intValue());
				}
			}

			@Override
			public Schema<E> type() {
				return valueType;
			}
		});
	}

	@Override
	public Iterable<? extends Property<List<E>, ?>> properties() {
		return Collections.emptyList();
	}

	@Override
	public Iterable<? extends Attribute<List<E>, ?>> attributes(final List<E> message) {
		return new Iterable<Attribute<List<E>,E>>(){
			@Override
			public Iterator<Attribute<List<E>, E>> iterator() {
				return new Iterator<Attribute<List<E>,E>>(){
					private int index;
					@Override
					public boolean hasNext() {
						return index < message.size();
					}

					@Override
					public Attribute<List<E>, E> next() {
						return property(index++).attribute(message);
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}
