/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.annotation.ElementType;
import java.util.LinkedList;
import java.util.TreeMap;

import org.fuwjin.jon.JonReader;
import org.fuwjin.pogo.state.ParseException;
import org.fuwjin.test.object.ComplexChild;
import org.fuwjin.test.object.IntegerObject;
import org.fuwjin.test.object.PrimitiveContainer;
import org.fuwjin.test.object.SimpleChild;
import org.fuwjin.test.object.SimpleGrandChild;
import org.fuwjin.test.object.SimpleGreatGrandChild;
import org.fuwjin.test.object.SimpleObject;
import org.junit.Test;

public class WhenReadingKnownTypes {
   @Test
   public void shouldReadArray() throws ParseException {
      final String[] result = (String[])new JonReader("(java.lang.String[])[\"hi\",\"hello\"]").read();
      assertThat(result, is(new String[]{"hi", "hello"}));
   }

   @Test
   public void shouldReadClass() throws ParseException {
      final Class<?> result = (Class<?>)new JonReader("(java.lang.Class)java.lang.String[]").read();
      assertEquals(result, String[].class);
   }

   @Test
   public void shouldReadDefaultComplexChild() throws ParseException {
      final ComplexChild result = (ComplexChild)new JonReader(
            "(org.fuwjin.test.object.ComplexChild){s:\"wow\",io:(org.fuwjin.test.object.IntegerChild){list:(java.util.ArrayList)[\"bob\",\"was\",\"here\"]|i:19}}")
            .read();
      assertThat(result, is(new ComplexChild()));
   }

   @Test
   public void shouldReadDefaultIntegerObject() throws ParseException {
      final IntegerObject result = (IntegerObject)new JonReader("(org.fuwjin.test.object.IntegerObject){i:15}").read();
      assertThat(result, is(new IntegerObject()));
   }

   @Test
   public void shouldReadDefaultObject() throws ParseException {
      final Object result = new JonReader("(java.lang.Object){}").read();
      assertThat(result, is(Object.class));
   }

   @Test
   public void shouldReadDefaultSimpleChild() throws ParseException {
      final SimpleChild result = (SimpleChild)new JonReader(
            "(org.fuwjin.test.object.SimpleChild){s:12.234|s:\"curious\",io:{i:181}}").read();
      assertThat(result, is(new SimpleChild()));
   }

   @Test
   public void shouldReadDefaultSimpleGrandChild() throws ParseException {
      final SimpleGrandChild result = (SimpleGrandChild)new JonReader(
            "(org.fuwjin.test.object.SimpleGrandChild){s:82.24|s:\"bland\",io:{i:134}}").read();
      assertThat(result, is(new SimpleGrandChild()));
   }

   @Test
   public void shouldReadDefaultSimpleGreatGrandChild() throws ParseException {
      final SimpleGreatGrandChild result = (SimpleGreatGrandChild)new JonReader(
            "(org.fuwjin.test.object.SimpleGreatGrandChild){s:[\"crazy\",\"train\"]|s:57.354|s:\"nestedness\",io:{i:235}}")
            .read();
      assertThat(result, is(new SimpleGreatGrandChild()));
   }

   @Test
   public void shouldReadDefaultSimpleObject() throws ParseException {
      final SimpleObject result = (SimpleObject)new JonReader(
            "(org.fuwjin.test.object.SimpleObject){s:\"howdy\",io:{i:17}}").read();
      assertThat(result, is(new SimpleObject()));
   }

   @Test
   public void shouldReadEnum() throws ParseException {
      final ElementType result = (ElementType)new JonReader("(java.lang.annotation.ElementType)TYPE").read();
      assertThat(result, is(ElementType.TYPE));
   }

   @Test
   public void shouldReadList() throws ParseException {
      final LinkedList<String> result = (LinkedList<String>)new JonReader("(java.util.LinkedList)[\"hi\",\"hello\"]")
            .read();
      final LinkedList<String> expected = new LinkedList<String>();
      expected.add("hi");
      expected.add("hello");
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadMap() throws ParseException {
      final TreeMap<String, String> result = (TreeMap<String, String>)new JonReader(
            "(java.util.TreeMap){\"hi\":\"mom\",\"hello\":\"world\"}").read();
      final TreeMap<String, String> expected = new TreeMap<String, String>();
      expected.put("hi", "mom");
      expected.put("hello", "world");
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadObject() throws ParseException {
      final PrimitiveContainer result = (PrimitiveContainer)new JonReader(
            "(org.fuwjin.test.object.PrimitiveContainer){index:17}").read();
      assertThat(result, is(new PrimitiveContainer(17)));
   }

   @Test
   public void shouldReadStringConstructor() throws ParseException {
      final StringBuilder result = (StringBuilder)new JonReader("(java.lang.StringBuilder)\"hi mom\"").read();
      assertThat(result.toString(), is("hi mom"));
   }

   @Test
   public void shouldReadValueOf() throws ParseException {
      final Long result = (Long)new JonReader("(java.lang.Long)100").read();
      assertThat(result, is(100L));
   }
}
