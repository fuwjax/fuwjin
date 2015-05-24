package org.fuwjin.test;

import static org.fuwjin.sample.SampleMessage.SCHEMA;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.fuwjin.sample.SampleMessage;
import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.impl.PrimitiveSchema;
import org.junit.Before;
import org.junit.Test;

public class MessageUsage {
	private SampleMessage message;

	@Before
	public void setup(){
		message = SCHEMA.create();
		assertThat(SCHEMA, is(message.schema()));
	}
	
	@Test
	public void testDirectUsage(){
		assertFalse(message.hasName());
		assertNull(message.getName());
		assertNull(message.setName("bob"));
		assertTrue(message.hasName());
		assertEquals("bob",message.getName());
		assertEquals("bob",message.clearName());
		assertFalse(message.hasName());
		assertNull(message.getName());
	}

	@Test
	public void testAttributeUsage(){
		Attribute<SampleMessage, ?> name = message.attribute("name");
		assertFalse(name.has());
		assertNull(name.get());
		set("name","bob");
		assertTrue(name.has());
		assertEquals("bob",name.get());
		assertEquals("bob",name.clear());
		assertFalse(name.has());
		assertNull(name.get());
	}

	@Test
	public void testTypedAttributeUsage(){
		Attribute<SampleMessage, String> name = message.attribute("name", PrimitiveSchema.STRING);
		assertFalse(name.has());
		assertNull(name.get());
		assertNull(name.set("bob"));
		assertTrue(name.has());
		assertEquals("bob",name.get());
		assertEquals("bob",name.clear());
		assertFalse(name.has());
		assertNull(name.get());
	}

	@Test
	public void testMissingAttributeUsage(){
		Attribute<SampleMessage, ?> age = message.attribute("age");
		assertFalse(age.has());
		assertNull(age.get());
		try{
			set("age",5);
			fail("should not be able to set non-existant property");
		}catch(RuntimeException e){
			// continue
		}
		assertFalse(age.has());
		assertNull(age.get());
		assertNull(age.clear());
		assertFalse(age.has());
		assertNull(age.get());
	}

	@Test
	public void testMissingTypedAttributeUsage(){
		Attribute<SampleMessage, Integer> age = message.attribute("age", PrimitiveSchema.INTEGER);
		assertFalse(age.has());
		assertNull(age.get());
		try{
			age.set(5);
			fail("should not be able to set non-existant property");
		}catch(RuntimeException e){
			// continue
		}
		assertFalse(age.has());
		assertNull(age.get());
		assertNull(age.clear());
		assertFalse(age.has());
		assertNull(age.get());
	}

	@Test
	public void testPropertyUsage(){
		Property<SampleMessage,?> name = SCHEMA.property("name");
		assertFalse(name.has(message));
		assertNull(name.get(message));
		set("name","bob");
		assertTrue(name.has(message));
		assertEquals("bob",name.get(message));
		assertEquals("bob",name.clear(message));
		assertFalse(name.has(message));
		assertNull(name.get(message));
	}

	@Test
	public void testTypedPropertyUsage(){
		Property<SampleMessage,String> name = SCHEMA.property("name", PrimitiveSchema.STRING);
		assertFalse(name.has(message));
		assertNull(name.get(message));
		assertNull(name.set(message,"bob"));
		assertTrue(name.has(message));
		assertEquals("bob",name.get(message));
		assertEquals("bob",name.clear(message));
		assertFalse(name.has(message));
		assertNull(name.get(message));
	}

	@Test
	public void testMissingPropertyUsage(){
		Property<SampleMessage,?> age = SCHEMA.property("age");
		assertFalse(age.has(message));
		assertNull(age.get(message));
		try{
			set("age",5);
			fail("should not be able to set non-existant property");
		}catch(RuntimeException e){
			// continue
		}
		assertFalse(age.has(message));
		assertNull(age.get(message));
		assertNull(age.clear(message));
		assertFalse(age.has(message));
		assertNull(age.get(message));
	}

	@Test
	public void testMissingTypedPropertyUsage(){
		Property<SampleMessage,Integer> age = SCHEMA.property("age", PrimitiveSchema.INTEGER);
		assertFalse(age.has(message));
		assertNull(age.get(message));
		try{
			age.set(message,5);
			fail("should not be able to set non-existant property");
		}catch(RuntimeException e){
			// continue
		}
		assertFalse(age.has(message));
		assertNull(age.get(message));
		assertNull(age.clear(message));
		assertFalse(age.has(message));
		assertNull(age.get(message));
	}

	@Test
	public void testDirectPropertyUsage(){
		Property<SampleMessage,String> name = SCHEMA.name;
		assertFalse(name.has(message));
		assertNull(name.get(message));
		assertNull(name.set(message,"bob"));
		assertTrue(name.has(message));
		assertThat("bob",is(name.get(message)));
		assertThat("bob",is(name.clear(message)));
		assertFalse(name.has(message));
		assertNull(name.get(message));
	}
	
	protected void set(Object key, Object value){
		message.attribute(key,PrimitiveSchema.OBJECT).set(value);
	}
}
