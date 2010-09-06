/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.annotation.ElementType;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fuwjin.jon.JonReader;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.test.object.ClassRef;
import org.fuwjin.test.object.ComplexChild;
import org.fuwjin.test.object.InnerChild;
import org.fuwjin.test.object.IntegerField;
import org.fuwjin.test.object.IntegerObject;
import org.fuwjin.test.object.PrimitiveContainer;
import org.fuwjin.test.object.SelfReferencingObject;
import org.fuwjin.test.object.SimpleChild;
import org.fuwjin.test.object.SimpleGrandChild;
import org.fuwjin.test.object.SimpleGreatGrandChild;
import org.fuwjin.test.object.SimpleObject;
import org.fuwjin.test.object.TransientChild;
import org.junit.Test;

public class WhenReadingReferencedTypes {
   @Test
   public void shouldReadArray() throws PogoException {
      final String[] result = (String[])new JonReader("&0=(&1=java.lang.String[])[\"hi\",\"hello\"]").read();
      assertThat(result, is(new String[]{"hi", "hello"}));
   }

   @Test
   public void shouldReadClass() throws PogoException {
      final Class<?> result = (Class<?>)new JonReader("&0=(&1=java.lang.Class)java.lang.String[]").read();
      assertEquals(result, String[].class);
   }

   @Test
   public void shouldReadDefaultClassRef() throws PogoException {
      final ClassRef result = (ClassRef)new JonReader("&0=(&1=org.fuwjin.test.object.ClassRef){cls:&1}").read();
      assertThat(result, is(new ClassRef()));
   }

   @Test
   public void shouldReadDefaultComplexChild() throws PogoException {
      final ComplexChild result = (ComplexChild)new JonReader(
            "&0=(&1=org.fuwjin.test.object.ComplexChild){s:\"wow\",io:&2=(&3=org.fuwjin.test.object.IntegerChild){list:&4=(&5=java.util.ArrayList)[\"bob\",\"was\",\"here\"]|i:19}}")
            .read();
      assertThat(result, is(new ComplexChild()));
   }

   @Test
   public void shouldReadDefaultInnerChild() throws PogoException {
      final JonReader reader = new JonReader("&0=(&1=org.fuwjin.test.object.InnerChild){inner:&2={i:1,this$0:&0}}");
      final InnerChild result = (InnerChild)reader.read();
      assertThat(result, is(new InnerChild()));
   }

   @Test
   public void shouldReadDefaultIntegerObject() throws PogoException {
      final IntegerObject result = (IntegerObject)new JonReader("&0=(&1=org.fuwjin.test.object.IntegerObject){i:15}")
            .read();
      assertThat(result, is(new IntegerObject()));
   }

   @Test
   public void shouldReadDefaultObject() throws PogoException {
      final Object result = new JonReader("&0=(&1=java.lang.Object){}").read();
      assertThat(result, is(Object.class));
   }

   @Test
   public void shouldReadDefaultSelfRefObject() throws PogoException {
      final SelfReferencingObject result = (SelfReferencingObject)new JonReader(
            "&0=(&1=org.fuwjin.test.object.SelfReferencingObject){obj:&0}").read();
      assertThat(result, sameInstance(result.getSelf()));
   }

   @Test
   public void shouldReadDefaultSimpleChild() throws PogoException {
      final SimpleChild result = (SimpleChild)new JonReader(
            "&0=(&1=org.fuwjin.test.object.SimpleChild){s:12.234|s:\"curious\",io:&2={i:181}}").read();
      assertThat(result, is(new SimpleChild()));
   }

   @Test
   public void shouldReadDefaultSimpleGrandChild() throws PogoException {
      final SimpleGrandChild result = (SimpleGrandChild)new JonReader(
            "&0=(&1=org.fuwjin.test.object.SimpleGrandChild){s:82.24|s:\"bland\",io:&2={i:134}}").read();
      assertThat(result, is(new SimpleGrandChild()));
   }

   @Test
   public void shouldReadDefaultSimpleGreatGrandChild() throws PogoException {
      final SimpleGreatGrandChild result = (SimpleGreatGrandChild)new JonReader(
            "&0=(&1=org.fuwjin.test.object.SimpleGreatGrandChild){s:[\"crazy\",\"train\"]|s:57.354|s:\"nestedness\",io:&2={i:235}}")
            .read();
      assertThat(result, is(new SimpleGreatGrandChild()));
   }

   @Test
   public void shouldReadDefaultSimpleObject() throws PogoException {
      final SimpleObject result = (SimpleObject)new JonReader(
            "&0=(&1=org.fuwjin.test.object.SimpleObject){s:\"howdy\",io:&2={i:17}}").read();
      assertThat(result, is(new SimpleObject()));
   }

   @Test
   public void shouldReadDefaultTransientChild() throws PogoException {
      final TransientChild result = (TransientChild)new JonReader(
            "&0=(&1=org.fuwjin.test.object.TransientChild){i:191}").read();
      assertThat(result, is(new TransientChild()));
   }

   @Test
   public void shouldReadDoubleArray() throws PogoException {
      final double[] result = (double[])new JonReader("&0=(&1=double[])[5.23,1.7E-13,-131.212]").read();
      assertThat(result, is(new double[]{5.23, 1.7E-13, -131.212}));
   }

   @Test
   public void shouldReadEnum() throws PogoException {
      final ElementType result = (ElementType)new JonReader("(&0=java.lang.annotation.ElementType)TYPE").read();
      assertThat(result, is(ElementType.TYPE));
   }

   @Test
   public void shouldReadList() throws PogoException {
      final LinkedList<String> result = (LinkedList<String>)new JonReader(
            "&0=(&1=java.util.LinkedList)[\"hi\",\"hello\"]").read();
      final LinkedList<String> expected = new LinkedList<String>();
      expected.add("hi");
      expected.add("hello");
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadMap() throws PogoException {
      final TreeMap<String, String> result = (TreeMap<String, String>)new JonReader(
            "&0=(&1=java.util.TreeMap){\"hi\":\"mom\",\"hello\":\"world\"}").read();
      final TreeMap<String, String> expected = new TreeMap<String, String>();
      expected.put("hi", "mom");
      expected.put("hello", "world");
      assertThat(result, is(expected));
   }

   @Test
   public void shouldReadNullInArray() throws PogoException {
      final String[] result = (String[])new JonReader("&0=(&1=java.lang.String[])[null]").read();
      assertThat(result, is(new String[]{null}));
   }

   @Test
   public void shouldReadNullStringField() throws PogoException {
      final SimpleObject result = (SimpleObject)new JonReader(
            "&0=(&1=org.fuwjin.test.object.SimpleObject){s:null,io:null}").read();
      assertThat(result, is(new SimpleObject(null, null)));
   }

   @Test
   public void shouldReadNullWrapperField() throws PogoException {
      final IntegerField result = (IntegerField)new JonReader("&0=(&1=org.fuwjin.test.object.IntegerField){i:null}")
            .read();
      assertThat(result, is(new IntegerField(null)));
   }

   @Test
   public void shouldReadObject() throws PogoException {
      final PrimitiveContainer result = (PrimitiveContainer)new JonReader(
            "&1=(&2=org.fuwjin.test.object.PrimitiveContainer){index:17}").read();
      assertThat(result, is(new PrimitiveContainer(17)));
   }

   @Test
   public void shouldReadReferenceNamedNull() throws PogoException {
      final Object result = new JonReader("&null=(&1=java.lang.Object){}").read();
      assertThat(result, is(Object.class));
   }

   @Test
   public void shouldReadSelfRefArray() throws PogoException {
      final Object[] result = (Object[])new JonReader("&0=(&1=java.lang.Object[])[&0]").read();
      assertThat(result, sameInstance(result[0]));
      assertThat(result.length, is(1));
   }

   @Test
   public void shouldReadSelfRefList() throws PogoException {
      final List<Object> result = (List<Object>)new JonReader("&0=(&1=java.util.ArrayList)[&0]").read();
      assertThat(result, sameInstance(result.get(0)));
      assertThat(result.size(), is(1));
   }

   @Test
   public void shouldReadSelfRefList2() throws PogoException {
      final List<Object> result = (List<Object>)new JonReader("&0=(&1=java.util.ArrayList)[&0,\"test\"]").read();
      assertThat(result, sameInstance(result.get(0)));
      assertThat((String)result.get(1), is("test"));
      assertThat(result.size(), is(2));
   }

   @Test
   public void shouldReadSelfRefMap() throws PogoException {
      final Map<Object, Object> result = (Map<Object, Object>)new JonReader("&0=(&1=java.util.IdentityHashMap){&0:&0}")
            .read();
      assertThat(result, sameInstance(result.get(result)));
      assertThat(result.size(), is(1));
   }

   @Test
   public void shouldReadSelfRefMapKey() throws PogoException {
      final Map<Object, String> result = (Map<Object, String>)new JonReader(
            "&0=(&1=java.util.IdentityHashMap){&0:\"test\"}").read();
      assertThat("test", is(result.get(result)));
      assertThat(result.size(), is(1));
   }

   @Test
   public void shouldReadSelfRefMapValue() throws PogoException {
      final Map<String, Object> result = (Map<String, Object>)new JonReader("&0=(&1=java.util.HashMap){\"test\":&0}")
            .read();
      assertThat(result, sameInstance(result.get("test")));
      assertThat(result.size(), is(1));
   }

   @Test
   public void shouldReadSingleList() throws PogoException {
      final List<String> result = (List<String>)new JonReader(
            "&0=(&1=java.util.Collections$SingletonList){element:\"a\"}").read();
      assertThat(result, is(Collections.singletonList("a")));
   }

   @Test
   public void shouldReadSingleMap() throws PogoException {
      final Map<String, String> result = (Map<String, String>)new JonReader(
            "&0=(&1=java.util.Collections$SingletonMap){k:\"a\",v:\"b\"}").read();
      assertThat(result, is(Collections.singletonMap("a", "b")));
   }

   @Test
   public void shouldReadStringConstructor() throws PogoException {
      final StringBuilder result = (StringBuilder)new JonReader("&0=(&1=java.lang.StringBuilder)\"hi mom\"").read();
      assertThat(result.toString(), is("hi mom"));
   }

   @Test
   public void shouldReadValueOf() throws PogoException {
      final Long result = (Long)new JonReader("(&0=java.lang.Long)100").read();
      assertThat(result, is(100L));
   }
}
