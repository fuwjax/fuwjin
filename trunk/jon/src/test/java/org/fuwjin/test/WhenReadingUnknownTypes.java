/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.jon.JonReader;
import org.fuwjin.pogo.PogoException;
import org.junit.Test;

public class WhenReadingUnknownTypes {
   @Test
   public void shouldReadBackslash() throws PogoException {
      final String result = (String)new JonReader("\"a\\\\backslash\"").read();
      assertThat(result, is("a\\backslash"));
   }

   @Test
   public void shouldReadClass() throws PogoException {
      final Class<?> result = (Class<?>)new JonReader("java.lang.Object").read();
      assertThat(result, is(Object.class));
   }

   @Test
   public void shouldReadDouble() throws PogoException {
      final double result = (Double)new JonReader("15234.8697").read();
      assertThat(result, is(15234.8697));
   }

   @Test
   public void shouldReadDoubleQuote() throws PogoException {
      final String result = (String)new JonReader("\"double\\\"quote\"").read();
      assertThat(result, is("double\"quote"));
   }

   @Test
   public void shouldReadDoubleWithExponent() throws PogoException {
      final double result = (Double)new JonReader("15234.8697e4").read();
      assertThat(result, is(15234.8697e4));
   }

   @Test
   public void shouldReadDoubleWithOnlyExponent() throws PogoException {
      final double result = (Double)new JonReader("15234e4").read();
      assertThat(result, is(15234e4));
   }

   @Test
   public void shouldReadEmptyList() throws PogoException {
      final Object[] result = (Object[])new JonReader("[]").read();
      assertThat(result, is(new Object[0]));
   }

   @Test
   public void shouldReadEntries() throws PogoException {
      final Map<String, String> result = (Map<String, String>)new JonReader("{\"hi\":\"mom\",\"hello\":\"world\"}")
            .read();
      final Map<String, String> expected = new HashMap<String, String>();
      expected.put("hi", "mom");
      expected.put("hello", "world");
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadExplicitDouble() throws PogoException {
      final double result = (Double)new JonReader("15234.8697D").read();
      assertThat(result, is(15234.8697));
   }

   @Test
   public void shouldReadExplicitDoubleWithExponent() throws PogoException {
      final double result = (Double)new JonReader("15234.8697e4D").read();
      assertThat(result, is(15234.8697e4));
   }

   @Test
   public void shouldReadExplicitDoubleWithNoPoint() throws PogoException {
      final double result = (Double)new JonReader("15234D").read();
      assertThat(result, is(15234D));
   }

   @Test
   public void shouldReadExplicitDoubleWithOnlyExponent() throws PogoException {
      final double result = (Double)new JonReader("15234e4D").read();
      assertThat(result, is(15234e4));
   }

   @Test
   public void shouldReadFalse() throws PogoException {
      final boolean result = (Boolean)new JonReader("false").read();
      assertFalse(result);
   }

   @Test
   public void shouldReadFloating() throws PogoException {
      final float result = (Float)new JonReader("15234.8697f").read();
      assertThat(result, is(15234.8697f));
   }

   @Test
   public void shouldReadFloatingWithExponent() throws PogoException {
      final float result = (Float)new JonReader("15234.8697e4f").read();
      assertThat(result, is(15234.8697e4f));
   }

   @Test
   public void shouldReadFloatingWithNoPoint() throws PogoException {
      final float result = (Float)new JonReader("15234f").read();
      assertThat(result, is(15234f));
   }

   @Test
   public void shouldReadFloatingWithOnlyExponent() throws PogoException {
      final float result = (Float)new JonReader("15234e4f").read();
      assertThat(result, is(15234e4f));
   }

   @Test
   public void shouldReadInt() throws PogoException {
      final int result = (Integer)new JonReader("15630239").read();
      assertThat(result, is(15630239));
   }

   @Test
   public void shouldReadLong() throws PogoException {
      final long result = (Long)new JonReader("15630239L").read();
      assertThat(result, is(15630239L));
   }

   @Test
   public void shouldReadNewLine() throws PogoException {
      final String result = (String)new JonReader("\"new\\nline\"").read();
      assertThat(result, is("new\nline"));
   }

   @Test
   public void shouldReadNoEntry() throws PogoException {
      final Map<Object, Object> result = (Map<Object, Object>)new JonReader("{}").read();
      final Map<Object, Object> expected = new HashMap<Object, Object>();
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadNull() throws PogoException {
      final Object result = new JonReader("null").read();
      assertNull(result);
   }

   @Test
   public void shouldReadReferences() throws PogoException {
      final Map<String, String> result = (Map<String, String>)new JonReader("{\"hi\":&1,\"hello\":&1=\"mom\"}").read();
      final Map<String, String> expected = new HashMap<String, String>();
      expected.put("hi", "mom");
      expected.put("hello", "mom");
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadSeveralElements() throws PogoException {
      final Object[] result = (Object[])new JonReader("[123,true,null,\"hi mom\"]").read();
      assertThat(result, is(new Object[]{123, true, null, "hi mom"}));
   }

   @Test
   public void shouldReadSingleElement() throws PogoException {
      final Object[] result = (Object[])new JonReader("[123]").read();
      assertThat(result, is(new Object[]{123}));
   }

   @Test
   public void shouldReadSingleEntry() throws PogoException {
      final Map<String, String> result = (Map<String, String>)new JonReader("{\"hi\":\"mom\"}").read();
      final Map<String, String> expected = new HashMap<String, String>();
      expected.put("hi", "mom");
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadString() throws PogoException {
      final String result = (String)new JonReader("\"hello world\"").read();
      assertThat(result, is("hello world"));
   }

   @Test
   public void shouldReadTab() throws PogoException {
      final String result = (String)new JonReader("\"a\\ttab\"").read();
      assertThat(result, is("a\ttab"));
   }

   @Test
   public void shouldReadTrue() throws PogoException {
      final Boolean result = (Boolean)new JonReader("true").read();
      assertTrue(result);
   }

   @Test
   public void shouldReadZero() throws PogoException {
      final int result = (Integer)new JonReader("0").read();
      assertThat(result, is(0));
   }

   @Test
   public void shouldReadZeroPointZero() throws PogoException {
      final double result = (Double)new JonReader("0.0").read();
      assertThat(result, is(0.0));
   }
}
