package org.fuwjin.test;

import static org.fuwjin.sample.SampleMessage.SCHEMA;
import static org.fuwjin.sample.SampleOverflowMessage.OVER_SCHEMA;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.fuwjin.sample.SampleMessage;
import org.fuwjin.sample.SampleOverflowMessage;
import org.junit.Test;

public class MessageUsage {
	@Test
	public void testUsage(){
		SampleMessage message = SCHEMA.create();
		assertThat(SCHEMA, is(message.schema()));
		assertFalse(message.attribute("name").has());
		SCHEMA.name.set(message, "bob");
		assertThat("bob",is(message.getName()));
		assertTrue(SCHEMA.property("name").attribute(message).has());
		assertFalse(message.attribute("age").has());
		assertNull(SCHEMA.property("age").attribute(message).get());
		try{
			message.attribute("age").set(5);
			fail("should not be able to set non-existant property");
		}catch(RuntimeException e){
			// continue
		}
	}
	
	@Test
	public void testOverflowUsage(){
		SampleOverflowMessage message = OVER_SCHEMA.create();
		assertThat(OVER_SCHEMA, is(message.schema()));
		assertFalse(message.attribute("name").has());
		OVER_SCHEMA.name.set(message, "bob");
		assertThat("bob",is(message.getName()));
		assertTrue(OVER_SCHEMA.property("name").attribute(message).has());
		message.attribute("name").clear();
		message.overflow().put("name", "jim");
		assertFalse(message.attribute("name").has());
		message.attribute("age").set(5);
		assertTrue(message.attribute("age").has());
		assertThat(5, is(OVER_SCHEMA.property("age").attribute(message).get()));
	}
}
