/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fuwjin.jon.JonWriter;
import org.fuwjin.pogo.PogoException;
import org.junit.Test;

/**
 * Jon should be able to write unknown types.
 */
public class WhenWritingUnknownTypes {
   /**
    * Jon should write strings with backslashes.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteBackslash() throws PogoException {
      final String result = new JonWriter().write("a\\backslash");
      assertThat(result, is("\"a\\\\backslash\""));
   }

   /**
    * Jon should write class literals.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteClass() throws PogoException {
      final String result = new JonWriter().write(Object.class);
      assertThat(result, is("&0=java.lang.Object"));
   }

   /**
    * Jon should write doubles.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteDouble() throws PogoException {
      final String result = new JonWriter().write(4.3);
      assertThat(result, is("4.3"));
   }

   /**
    * Jon should write strings with double quotes.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteDoubleQuote() throws PogoException {
      final String result = new JonWriter().write("double\"quote");
      assertThat(result, is("\"double\\\"quote\""));
   }

   /**
    * Jon should write doubles with exponents.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteDoubleWithExponent() throws PogoException {
      final String result = new JonWriter().write(15234.8697e4);
      assertThat(result, is("1.52348697E8"));
   }

   /**
    * Jon should write doubles with exponents but no decimal.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteDoubleWithOnlyExponent() throws PogoException {
      final String result = new JonWriter().write(15234e4);
      assertThat(result, is("1.5234E8"));
   }

   /**
    * Jon should write empty arrays.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteEmptyList() throws PogoException {
      final String result = new JonWriter().write(new Object[0]);
      assertThat(result, is("&0=(&1=java.lang.Object[])[]"));
   }

   /**
    * Jon should write maps.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteEntries() throws PogoException {
      final Map<String, String> expected = new LinkedHashMap<String, String>();
      expected.put("hi", "mom");
      expected.put("hello", "world");
      final String result = new JonWriter().write(expected);
      assertThat(result, is("&0=(&1=java.util.LinkedHashMap){\"hi\":\"mom\",\"hello\":\"world\"}"));
   }

   /**
    * Jon should write decimals.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteExplicitDouble() throws PogoException {
      final String result = new JonWriter().write(15234.8697);
      assertThat(result, is("15234.8697"));
   }

   /**
    * Jon should write a double.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteExplicitDoubleWithExponent() throws PogoException {
      final String result = new JonWriter().write(15234.8697e4);
      assertThat(result, is("1.52348697E8"));
   }

   /**
    * Jon should write a double with a double identifier.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteExplicitDoubleWithNoPoint() throws PogoException {
      final String result = new JonWriter().write(15234D);
      assertThat(result, is("15234.0"));
   }

   /**
    * Jon should write doubles with exponents.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteExplicitDoubleWithOnlyExponent() throws PogoException {
      final String result = new JonWriter().write(15234e4);
      assertThat(result, is("1.5234E8"));
   }

   /**
    * Jon should write false.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteFalse() throws PogoException {
      final String result = new JonWriter().write(false);
      assertThat(result, is("false"));
   }

   /**
    * Jon should write floats.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteFloating() throws PogoException {
      final String result = new JonWriter().write(15234.87f);
      assertThat(result, is("15234.87F"));
   }

   /**
    * Jon should write floats with exponents.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteFloatingWithExponent() throws PogoException {
      final String result = new JonWriter().write(15234.87e4f);
      assertThat(result, is("1.52348704E8F"));
   }

   /**
    * Jon should write floats with float specifiers.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteFloatingWithNoPoint() throws PogoException {
      final String result = new JonWriter().write(15234f);
      assertThat(result, is("15234.0F"));
   }

   /**
    * Jon should write floats with exponents.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteFloatingWithOnlyExponent() throws PogoException {
      final String result = new JonWriter().write(15234e4f);
      assertThat(result, is("1.5234E8F"));
   }

   /**
    * Jon should serialize ints.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteInt() throws PogoException {
      final String result = new JonWriter().write(15630239);
      assertThat(result, is("15630239"));
   }

   /**
    * Apparently Jon should also serialize ints again.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteInteger() throws PogoException {
      final String result = new JonWriter().write(123);
      assertThat(result, is("123"));
   }

   /**
    * Jon should serialize longs.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteLong() throws PogoException {
      final String result = new JonWriter().write(15630239L);
      assertThat(result, is("15630239L"));
   }

   /**
    * Jon should write string literals with new lines.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteNewLine() throws PogoException {
      final String result = new JonWriter().write("new\nline");
      assertThat(result, is("\"new\\nline\""));
   }

   /**
    * Jon should write an empty map.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteNoEntry() throws PogoException {
      final String result = new JonWriter().write(new HashMap<Object, Object>());
      assertThat(result, is("&0=(&1=java.util.HashMap){}"));
   }

   /**
    * Jon should write nulls.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteNull() throws PogoException {
      final String result = new JonWriter().write(null);
      assertThat(result, is("null"));
   }

   /**
    * Jon should write a new object.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteObject() throws PogoException {
      final String result = new JonWriter().write(new Object());
      assertThat(result, is("&0=(&1=java.lang.Object){}"));
   }

   /**
    * Jon should write an array of elements.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteSeveralElements() throws PogoException {
      final String result = new JonWriter().write(new Object[]{123, true, null, "hi mom"});
      assertThat(result, is("&0=(&1=java.lang.Object[])[123,true,null,\"hi mom\"]"));
   }

   /**
    * Jon should write an array of one element.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteSingleElement() throws PogoException {
      final String result = new JonWriter().write(new Object[]{123});
      assertThat(result, is("&0=(&1=java.lang.Object[])[123]"));
   }

   /**
    * Jon should write a hash map with one mapping.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteSingleEntry() throws PogoException {
      final Map<String, String> expected = new HashMap<String, String>();
      expected.put("hi", "mom");
      final String result = new JonWriter().write(expected);
      assertThat(result, is("&0=(&1=java.util.HashMap){\"hi\":\"mom\"}"));
   }

   /**
    * Jon should write a string literal.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteString() throws PogoException {
      final String result = new JonWriter().write("hi");
      assertThat(result, is("\"hi\""));
   }

   /**
    * Jon should write a string literal with a tab.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteTab() throws PogoException {
      final String result = new JonWriter().write("a\ttab");
      assertThat(result, is("\"a\\ttab\""));
   }

   /**
    * Jon should write true.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteTrue() throws PogoException {
      final String result = new JonWriter().write(true);
      assertThat(result, is("true"));
   }

   /**
    * Jon should write a zero.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteZero() throws PogoException {
      final String result = new JonWriter().write(0);
      assertThat(result, is("0"));
   }

   /**
    * Jon should write a zero double.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldWriteZeroPointZero() throws PogoException {
      final String result = new JonWriter().write(0.0);
      assertThat(result, is("0.0"));
   }
}
