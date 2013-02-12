package org.fuwjin.test;

import static org.junit.Assert.assertEquals;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.fuwjin.diioc.Diioc;
import org.fuwjin.diioc.Provides;
import org.fuwjin.sample.NamedObject;
import org.fuwjin.sample.SimpleConstructorInjection;
import org.fuwjin.sample.SimpleFieldInjection;
import org.fuwjin.sample.SimpleSample;
import org.fuwjin.sample.SimpleSetterInjection;
import org.fuwjin.sample.SimpleSingleton;
import org.junit.Test;

public class DiiocTest {
	@Test
	public void testSimpleSample() throws Exception {
		SimpleSample obj = new Diioc().create(SimpleSample.class);
		assertEquals("simple sample", obj.toString());
	}

	@Test
	public void testSimpleFieldInjection() throws Exception {
		SimpleFieldInjection obj = new Diioc().create(SimpleFieldInjection.class);
		assertEquals("simple field = simple sample", obj.toString());
	}

	@Test
	public void testSimpleSetterInjection() throws Exception {
		SimpleSetterInjection obj = new Diioc().create(SimpleSetterInjection.class);
		assertEquals("simple setter = simple sample", obj.toString());
	}

	@Test
	public void testSimpleConstructorInjection() throws Exception {
		SimpleConstructorInjection obj = new Diioc().create(SimpleConstructorInjection.class);
		assertEquals("simple constructor = simple sample", obj.toString());
	}

	@Test
	public void testDirectSample() throws Exception {
		SimpleSample obj = new Diioc(new SimpleSample("direct")).create(SimpleSample.class);
		assertEquals("direct", obj.toString());
	}

	@Test
	public void testDirectFieldInjection() throws Exception {
		SimpleFieldInjection obj = new Diioc(new SimpleSample("direct")).create(SimpleFieldInjection.class);
		assertEquals("simple field = direct", obj.toString());
	}

	@Test
	public void testDirectSetterInjection() throws Exception {
		SimpleSetterInjection obj = new Diioc(new SimpleSample("direct")).create(SimpleSetterInjection.class);
		assertEquals("simple setter = direct", obj.toString());
	}

	@Test
	public void testDirectConstructorInjection() throws Exception {
		SimpleConstructorInjection obj = new Diioc(new SimpleSample("direct")).create(SimpleConstructorInjection.class);
		assertEquals("simple constructor = direct", obj.toString());
	}

	@Test
	public void testSingleton() throws Exception {
		SimpleSample obj = new Diioc(new Object(){
			@Singleton SimpleSample sample = new SimpleSample("custom");
		}).create(SimpleSample.class);
		assertEquals("custom", obj.toString());
	}

	@Test
	public void testSingletonClass() throws Exception {
		SimpleSingleton obj = new Diioc(new Object(){
			SimpleSingleton sample = new SimpleSingleton("singleton");
		}).create(SimpleSingleton.class);
		assertEquals("singleton", obj.toString());
	}

	@Test
	public void testSingletonInjection() throws Exception {
		SimpleSetterInjection obj = new Diioc(new Object(){
			@Singleton SimpleSample sample = new SimpleSample("custom");
		}).create(SimpleSetterInjection.class);
		assertEquals("simple setter = custom", obj.toString());
	}

	@Test
	public void testNonSingletonInjection() throws Exception {
		SimpleSetterInjection obj = new Diioc(new Object(){
			SimpleSample sample = new SimpleSample("custom");
		}).create(SimpleSetterInjection.class);
		assertEquals("simple setter = custom", obj.toString());
	}

	@Test
	public void testProvides() throws Exception {
		SimpleSample obj = new Diioc(new Object(){
			@Provides SimpleSample sample(){
				return new SimpleSample("provided");
			}
		}).create(SimpleSample.class);
		assertEquals("provided", obj.toString());
	}

	@Test
	public void testProvidesInjection() throws Exception {
		SimpleFieldInjection obj = new Diioc(new Object(){
			@Provides SimpleSample sample(){
				return new SimpleSample("provided");
			}
		}).create(SimpleFieldInjection.class);
		assertEquals("simple field = provided", obj.toString());
	}

	@Test
	public void testNonProvidesInjection() throws Exception {
		SimpleFieldInjection obj = new Diioc(new Object(){
			SimpleSample sample(){
				return new SimpleSample("provided");
			}
		}).create(SimpleFieldInjection.class);
		assertEquals("simple field = simple sample", obj.toString());
	}

	@Test
	public void testProvider() throws Exception {
		SimpleSample obj = new Diioc(new Object(){
			Provider<SimpleSample> provider = new Provider<SimpleSample>(){
				@Override
				public SimpleSample get() {
					return new SimpleSample("provider");
				}
			};
		}).create(SimpleSample.class);
		assertEquals("provider", obj.toString());
	}

	@Test
	public void testProviderInjection() throws Exception {
		SimpleConstructorInjection obj = new Diioc(new Object(){
			Provider<SimpleSample> provider = new Provider<SimpleSample>(){
				@Override
				public SimpleSample get() {
					return new SimpleSample("provider");
				}
			};
		}).create(SimpleConstructorInjection.class);
		assertEquals("simple constructor = provider", obj.toString());
	}

	@Test
	public void testNamedInjection() throws Exception {
		NamedObject obj = new Diioc(new Object(){
			@Named("name") String objName = "diioc";
		}).create(NamedObject.class);
		assertEquals("name = diioc", obj.toString());
	}

	@Test
	public void testImplicitNamedInjection() throws Exception {
		NamedObject obj = new Diioc(new Object(){
			String name = "diioc";
		}).create(NamedObject.class);
		assertEquals("name = diioc", obj.toString());
	}
}
