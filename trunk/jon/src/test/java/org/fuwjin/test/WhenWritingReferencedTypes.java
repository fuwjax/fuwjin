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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.fuwjin.jon.JonWriter;
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

public class WhenWritingReferencedTypes {
   @Test
   public void shouldWriteArray() throws PogoException {
      final String result = new JonWriter().write(new String[]{"hi", "hello"});
      assertThat(result, is("&0=(&1=java.lang.String[])[\"hi\",\"hello\"]"));
   }

   @Test
   public void shouldWriteClass() throws PogoException {
      final String result = new JonWriter().write(String[].class);
      assertEquals(result, "&0=java.lang.String[]");
   }

   @Test
   public void shouldWriteDefaultClassRef() throws PogoException {
      final String result = new JonWriter().write(new ClassRef());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.ClassRef){cls:&1}"));
   }

   @Test
   public void shouldWriteDefaultComplexChild() throws PogoException {
      final String result = new JonWriter().write(new ComplexChild());
      assertThat(
            result,
            is("&0=(&1=org.fuwjin.test.object.ComplexChild){s:\"wow\",io:&2=(&3=org.fuwjin.test.object.IntegerChild){list:&4=(&5=java.util.ArrayList)[\"bob\",\"was\",\"here\"]|i:19}}"));
   }

   @Test
   public void shouldWriteDefaultInnerChild() throws PogoException {
      final String result = new JonWriter().write(new InnerChild());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.InnerChild){inner:&2={i:1,this$0:&0}}"));
   }

   @Test
   public void shouldwriteDefaultIntegerObject() throws PogoException {
      final String result = new JonWriter().write(new IntegerObject());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.IntegerObject){i:15}"));
   }

   @Test
   public void shouldwriteDefaultObject() throws PogoException {
      final String result = new JonWriter().write(new Object());
      assertThat(result, is("&0=(&1=java.lang.Object){}"));
   }

   @Test
   public void shouldwriteDefaultSelfRefObject() throws PogoException {
      final String result = new JonWriter().write(new SelfReferencingObject());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.SelfReferencingObject){obj:&0}"));
   }

   @Test
   public void shouldwriteDefaultSimpleChild() throws PogoException {
      final String result = new JonWriter().write(new SimpleChild());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.SimpleChild){s:12.234|s:\"curious\",io:&2={i:181}}"));
   }

   @Test
   public void shouldwriteDefaultSimpleGrandChild() throws PogoException {
      final String result = new JonWriter().write(new SimpleGrandChild());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.SimpleGrandChild){s:82.24|s:\"bland\",io:&2={i:134}}"));
   }

   @Test
   public void shouldwriteDefaultSimpleGreatGrandChild() throws PogoException {
      final String result = new JonWriter().write(new SimpleGreatGrandChild());
      assertThat(
            result,
            is("&0=(&1=org.fuwjin.test.object.SimpleGreatGrandChild){s:&2=[\"crazy\",\"train\"]|s:57.354|s:\"nestedness\",io:&3={i:235}}"));
   }

   @Test
   public void shouldwriteDefaultSimpleObject() throws PogoException {
      final String result = new JonWriter().write(new SimpleObject());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.SimpleObject){s:\"howdy\",io:&2={i:17}}"));
   }

   @Test
   public void shouldwriteDefaultTransientChild() throws PogoException {
      final String result = new JonWriter().write(new TransientChild());
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.TransientChild){i:191}"));
   }

   @Test
   public void shouldwriteDoubleArray() throws PogoException {
      final String result = new JonWriter().write(new double[]{5.23, 1.7E-13, -131.212});
      assertThat(result, is("&0=(&1=double[])[5.23,1.7E-13,-131.212]"));
   }

   @Test
   public void shouldwriteEnum() throws PogoException {
      final String result = new JonWriter().write(ElementType.TYPE);
      assertThat(result, is("(&0=java.lang.annotation.ElementType)TYPE"));
   }

   @Test
   public void shouldwriteList() throws PogoException {
      final LinkedList<String> expected = new LinkedList<String>();
      expected.add("hi");
      expected.add("hello");
      final String result = new JonWriter().write(expected);
      assertThat(result, is("&0=(&1=java.util.LinkedList)[\"hi\",\"hello\"]"));
   }

   @Test
   public void shouldwriteMap() throws PogoException {
      final Map<String, String> expected = new LinkedHashMap<String, String>();
      expected.put("hi", "mom");
      expected.put("hello", "world");
      final String result = new JonWriter().write(expected);
      assertThat(result, is("&0=(&1=java.util.LinkedHashMap){\"hi\":\"mom\",\"hello\":\"world\"}"));
   }

   @Test
   public void shouldwriteNullInArray() throws PogoException {
      final String result = new JonWriter().write(new String[]{null});
      assertThat(result, is("&0=(&1=java.lang.String[])[null]"));
   }

   @Test
   public void shouldwriteNullStringField() throws PogoException {
      final String result = new JonWriter().write(new SimpleObject(null, null));
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.SimpleObject){s:null,io:null}"));
   }

   @Test
   public void shouldwriteNullWrapperField() throws PogoException {
      final String result = new JonWriter().write(new IntegerField(null));
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.IntegerField){i:null}"));
   }

   @Test
   public void shouldwriteObject() throws PogoException {
      final String result = new JonWriter().write(new PrimitiveContainer(17));
      assertThat(result, is("&0=(&1=org.fuwjin.test.object.PrimitiveContainer){index:17}"));
   }

   @Test
   public void shouldwriteSelfRefArray() throws PogoException {
      final Object[] expected = new Object[1];
      expected[0] = expected;
      final String result = new JonWriter().write(expected);
      assertThat(result, is("&0=(&1=java.lang.Object[])[&0]"));
   }

   @Test
   public void shouldwriteSelfRefList() throws PogoException {
      final List<List<?>> list = new ArrayList<List<?>>();
      list.add(list);
      final String result = new JonWriter().write(list);
      assertThat(result, is("&0=(&1=java.util.ArrayList)[&0]"));
   }

   @Test
   public void shouldwriteSelfRefList2() throws PogoException {
      final List<Object> list = new ArrayList<Object>();
      list.add(list);
      list.add("test");
      final String result = new JonWriter().write(list);
      assertThat(result, is("&0=(&1=java.util.ArrayList)[&0,\"test\"]"));
   }

   @Test
   public void shouldwriteSelfRefMap() throws PogoException {
      final Map<Object, Object> map = new IdentityHashMap<Object, Object>();
      map.put(map, map);
      final String result = new JonWriter().write(map);
      assertThat(result, is("&0=(&1=java.util.IdentityHashMap){&0:&0}"));
   }

   @Test
   public void shouldwriteSelfRefMapKey() throws PogoException {
      final Map<Object, String> map = new IdentityHashMap<Object, String>();
      map.put(map, "test");
      final String result = new JonWriter().write(map);
      assertThat(result, is("&0=(&1=java.util.IdentityHashMap){&0:\"test\"}"));
   }

   @Test
   public void shouldwriteSelfRefMapValue() throws PogoException {
      final Map<String, Object> map = new HashMap<String, Object>();
      map.put("test", map);
      final String result = new JonWriter().write(map);
      assertThat(result, is("&0=(&1=java.util.HashMap){\"test\":&0}"));
   }

   @Test
   public void shouldwriteSingleList() throws PogoException {
      final String result = new JonWriter().write(Collections.singletonList("a"));
      assertThat(result, is("&0=(&1=java.util.Collections$SingletonList){element:\"a\"}"));
   }

   @Test
   public void shouldwriteSingleMap() throws PogoException {
      final String result = new JonWriter().write(Collections.singletonMap("a", "b"));
      assertThat(result, is("&0=(&1=java.util.Collections$SingletonMap){k:\"a\",v:\"b\"}"));
   }

   @Test
   public void shouldwriteStringConstructor() throws PogoException {
      final String result = new JonWriter().write(new StringBuilder("hi mom"));
      assertThat(result, is("&0=(&1=java.lang.StringBuilder)\"hi mom\""));
   }

   @Test
   public void shouldwriteValueOf() throws PogoException {
      final String result = new JonWriter().write(100L);
      assertThat(result, is("100L"));
   }
}
