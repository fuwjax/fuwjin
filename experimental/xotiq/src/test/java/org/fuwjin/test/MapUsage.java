package org.fuwjin.test;

import static org.fuwjin.xotiq.impl.PrimitiveSchema.INTEGER;
import static org.fuwjin.xotiq.impl.PrimitiveSchema.STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.impl.MapSchema;
import org.fuwjin.xotiq.impl.PrimitiveSchema;
import org.junit.Before;
import org.junit.Test;

public class MapUsage {
	private static final MapSchema<String, Integer> SCHEMA = new MapSchema<String,Integer>(STRING, INTEGER);
	private Map<String,Integer> message;

	@Before
	public void setup(){
		message = SCHEMA.create();
	}
	
	@Test
	public void testDirectUsage(){
		assertFalse(message.containsKey("name"));
		assertNull(message.get("name"));
		assertNull(message.put("name",7));
		assertTrue(message.containsKey("name"));
		assertEquals(7,message.get("name").intValue());
		assertEquals(7,message.remove("name").intValue());
		assertFalse(message.containsKey("name"));
		assertNull(message.get("name"));
	}

	@Test
	public void testAttributeUsage(){
		Attribute<Map<String,Integer>, Integer> name = SCHEMA.property("name").attribute(message);
		assertFalse(name.has());
		assertNull(name.get());
		assertNull(name.set(7));
		assertTrue(name.has());
		assertEquals(7,name.get().intValue());
		assertEquals(7,name.clear().intValue());
		assertFalse(name.has());
		assertNull(name.get());
	}

	@Test
	public void testTypedAttributeUsage(){
		Attribute<Map<String,Integer>, Integer> name = SCHEMA.property("name").attribute(message, INTEGER);
		assertFalse(name.has());
		assertNull(name.get());
		assertNull(name.set(7));
		assertTrue(name.has());
		assertEquals(7,name.get().intValue());
		assertEquals(7,name.clear().intValue());
		assertFalse(name.has());
		assertNull(name.get());
	}
  
	@Test
	public void testMissingPropertyUsage(){
		Property<Map<String,Integer>,Integer> age = SCHEMA.property("age");
		assertFalse(age.has(message));
		assertNull(age.get(message));
		set("age",5);
		assertTrue(age.has(message));
		assertEquals(5,age.get(message).intValue());
		assertEquals(5,age.clear(message).intValue());
		assertFalse(age.has(message));
		assertNull(age.get(message));
	}

	@Test
	public void testMissingTypedPropertyUsage(){
		Property<Map<String,Integer>,Integer> age = SCHEMA.property("age", PrimitiveSchema.INTEGER);
		assertFalse(age.has(message));
		assertNull(age.get(message));
		age.set(message,5);
		assertTrue(age.has(message));
		assertEquals(5,age.get(message).intValue());
		assertEquals(5,age.clear(message).intValue());
		assertFalse(age.has(message));
		assertNull(age.get(message));
	}
	
	protected void set(String key, Integer value){
		message.put(key,value);
	}
}
