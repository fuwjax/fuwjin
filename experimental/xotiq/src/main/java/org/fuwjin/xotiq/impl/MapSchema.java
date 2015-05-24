package org.fuwjin.xotiq.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class MapSchema<K, V> extends AbstractSchema<Map<K, V>> {
	private Schema<V> valueType;
	private Schema<K> keyType;
	
	public MapSchema(Schema<K> keyType, Schema<V> valueType){
		this.keyType = keyType;
		this.valueType = valueType;
	}
	
	@Override
	public Map<K, V> create() {
		return new HashMap<K,V>();
	}
	
	@Override
	public Map<K, V> convert(Object value) {
		return (Map<K,V>)value;
	}
	
	@Override
	public boolean isMessage(Object message) {
		return message instanceof Map;
	}

	@Override
	public Property<Map<K, V>, V> property(Object key) {
		final K realKey = keyType.convert(key);
		return new BaseProperty<Map<K,V>, V>(this, key, new PropertyActor<Map<K,V>,V>(){
			@Override
			public V get(Map<K, V> message) {
				return message.get(realKey);
			}

			@Override
			public void set(Map<K, V> message, V value) {
				message.put(realKey, value);
			}

			@Override
			public boolean has(Map<K, V> message) {
				return message.containsKey(realKey);
			}

			@Override
			public void clear(Map<K, V> message) {
				message.remove(realKey);
			}

			@Override
			public Schema<V> type() {
				return valueType;
			}
		});
	}

	@Override
	public Iterable<? extends Property<Map<K, V>, ?>> properties() {
		return Collections.emptyList();
	}

	@Override
	public Iterable<Attribute<Map<K,V>,V>> attributes(final Map<K,V> message) {
		return Iterables.transform(message.keySet(), new Function<K,Attribute<Map<K,V>,V>>(){
			public Attribute<Map<K,V>,V> apply(K key){
				return property(key).attribute(message);
			}
		});
	}
}
