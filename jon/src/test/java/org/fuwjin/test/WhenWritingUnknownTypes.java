/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fuwjin.io.PogoException;
import org.fuwjin.jon.JonWriter;
import org.junit.Test;

public class WhenWritingUnknownTypes {
   @Test
   public void shouldWriteBackslash() throws PogoException {
      final String result = new JonWriter().write("a\\backslash");
      assertThat(result, is("\"a\\\\backslash\""));
   }

   @Test
   public void shouldWriteClass() throws PogoException {
      final String result = new JonWriter().write(Object.class);
      assertThat(result, is("&0=java.lang.Object"));
   }

   @Test
   public void shouldWriteDouble() throws PogoException {
      final String result = new JonWriter().write(4.3);
      assertThat(result, is("4.3"));
   }

   @Test
   public void shouldWriteDoubleQuote() throws PogoException {
      final String result = new JonWriter().write("double\"quote");
      assertThat(result, is("\"double\\\"quote\""));
   }

   @Test
   public void shouldWriteDoubleWithExponent() throws PogoException {
      final String result = new JonWriter().write(15234.8697e4);
      assertThat(result, is("1.52348697E8"));
   }

   @Test
   public void shouldWriteDoubleWithOnlyExponent() throws PogoException {
      final String result = new JonWriter().write(15234e4);
      assertThat(result, is("1.5234E8"));
   }

   @Test
   public void shouldWriteEmptyList() throws PogoException {
      final String result = new JonWriter().write(new Object[0]);
      assertThat(result, is("&0=(&1=java.lang.Object[])[]"));
   }

   @Test
   public void shouldWriteEntries() throws PogoException {
      final Map<String, String> expected = new LinkedHashMap<String, String>();
      expected.put("hi", "mom");
      expected.put("hello", "world");
      final String result = new JonWriter().write(expected);
      assertThat(result, is("&0=(&1=java.util.LinkedHashMap){\"hi\":\"mom\",\"hello\":\"world\"}"));
   }

   @Test
   public void shouldWriteExplicitDouble() throws PogoException {
      final String result = new JonWriter().write(15234.8697);
      assertThat(result, is("15234.8697"));
   }

   @Test
   public void shouldWriteExplicitDoubleWithExponent() throws PogoException {
      final String result = new JonWriter().write(15234.8697e4);
      assertThat(result, is("1.52348697E8"));
   }

   @Test
   public void shouldWriteExplicitDoubleWithNoPoint() throws PogoException {
      final String result = new JonWriter().write(15234D);
      assertThat(result, is("15234.0"));
   }

   @Test
   public void shouldWriteExplicitDoubleWithOnlyExponent() throws PogoException {
      final String result = new JonWriter().write(15234e4);
      assertThat(result, is("1.5234E8"));
   }

   @Test
   public void shouldWriteFalse() throws PogoException {
      final String result = new JonWriter().write(false);
      assertThat(result, is("false"));
   }

   @Test
   public void shouldWriteFloating() throws PogoException {
      final String result = new JonWriter().write(15234.87f);
      assertThat(result, is("15234.87F"));
   }

   @Test
   public void shouldWriteFloatingWithExponent() throws PogoException {
      final String result = new JonWriter().write(15234.87e4f);
      assertThat(result, is("1.52348704E8F"));
   }

   @Test
   public void shouldWriteFloatingWithNoPoint() throws PogoException {
      final String result = new JonWriter().write(15234f);
      assertThat(result, is("15234.0F"));
   }

   @Test
   public void shouldWriteFloatingWithOnlyExponent() throws PogoException {
      final String result = new JonWriter().write(15234e4f);
      assertThat(result, is("1.5234E8F"));
   }

   /**
    * @throws FileNotFoundException
    * @throws PogoException
    */
   @Test
   public void shouldWriteInt() throws FileNotFoundException, PogoException {
      final String result = new JonWriter().write(15630239);
      assertThat(result, is("15630239"));
   }

   @Test
   public void shouldWriteInteger() throws PogoException {
      final String result = new JonWriter().write(123);
      assertThat(result, is("123"));
   }

   @Test
   public void shouldWriteLong() throws PogoException {
      final String result = new JonWriter().write(15630239L);
      assertThat(result, is("15630239L"));
   }

   /**
    * @throws FileNotFoundException
    * @throws PogoException
    */
   @Test
   public void shouldWriteNewLine() throws FileNotFoundException, PogoException {
      final String result = new JonWriter().write("new\nline");
      assertThat(result, is("\"new\\nline\""));
   }

   @Test
   public void shouldWriteNoEntry() throws PogoException {
      final String result = new JonWriter().write(new HashMap<Object, Object>());
      assertThat(result, is("&0=(&1=java.util.HashMap){}"));
   }

   @Test
   public void shouldWriteNull() throws PogoException {
      final String result = new JonWriter().write(null);
      assertThat(result, is("null"));
   }

   @Test
   public void shouldWriteObject() throws PogoException {
      final String result = new JonWriter().write(new Object());
      assertThat(result, is("&0=(&1=java.lang.Object){}"));
   }

   @Test
   public void shouldWriteSeveralElements() throws PogoException {
      final String result = new JonWriter().write(new Object[]{123, true, null, "hi mom"});
      assertThat(result, is("&0=(&1=java.lang.Object[])[123,true,null,\"hi mom\"]"));
   }

   @Test
   public void shouldWriteSingleElement() throws PogoException {
      final String result = new JonWriter().write(new Object[]{123});
      assertThat(result, is("&0=(&1=java.lang.Object[])[123]"));
   }

   @Test
   public void shouldWriteSingleEntry() throws PogoException {
      final Map<String, String> expected = new HashMap<String, String>();
      expected.put("hi", "mom");
      final String result = new JonWriter().write(expected);
      assertThat(result, is("&0=(&1=java.util.HashMap){\"hi\":\"mom\"}"));
   }

   @Test
   public void shouldWriteString() throws PogoException {
      final String result = new JonWriter().write("hi");
      assertThat(result, is("\"hi\""));
   }

   @Test
   public void shouldWriteTab() throws PogoException {
      final String result = new JonWriter().write("a\ttab");
      assertThat(result, is("\"a\\ttab\""));
   }

   @Test
   public void shouldWriteTrue() throws PogoException {
      final String result = new JonWriter().write(true);
      assertThat(result, is("true"));
   }

   @Test
   public void shouldWriteZero() throws PogoException {
      final String result = new JonWriter().write(0);
      assertThat(result, is("0"));
   }

   @Test
   public void shouldWriteZeroPointZero() throws PogoException {
      final String result = new JonWriter().write(0.0);
      assertThat(result, is("0.0"));
   }
}
