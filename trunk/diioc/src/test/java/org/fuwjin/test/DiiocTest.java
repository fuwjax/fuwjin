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
import org.junit.Ignore;
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
	public void testSingleton() throws Exception {
		SimpleSample obj = new Diioc().create(SimpleSample.class, new Object(){
			@Singleton SimpleSample sample = new SimpleSample("custom");
		});
		assertEquals("custom", obj.toString());
	}

	@Test
	public void testSingletonInjection() throws Exception {
		SimpleSetterInjection obj = new Diioc().create(SimpleSetterInjection.class, new Object(){
			@Singleton SimpleSample sample = new SimpleSample("custom");
		});
		assertEquals("simple setter = custom", obj.toString());
	}

	@Test
	public void testNonSingletonInjection() throws Exception {
		SimpleSetterInjection obj = new Diioc().create(SimpleSetterInjection.class, new Object(){
			SimpleSample sample = new SimpleSample("custom");
		});
		assertEquals("simple setter = simple sample", obj.toString());
	}

	@Test
	public void testProvides() throws Exception {
		SimpleSample obj = new Diioc().create(SimpleSample.class, new Object(){
			@Provides SimpleSample sample(){
				return new SimpleSample("provided");
			}
		});
		assertEquals("provided", obj.toString());
	}

	@Test
	public void testProvidesInjection() throws Exception {
		SimpleFieldInjection obj = new Diioc().create(SimpleFieldInjection.class, new Object(){
			@Provides SimpleSample sample(){
				return new SimpleSample("provided");
			}
		});
		assertEquals("simple field = provided", obj.toString());
	}

	@Test
	public void testNonProvidesInjection() throws Exception {
		SimpleFieldInjection obj = new Diioc().create(SimpleFieldInjection.class, new Object(){
			SimpleSample sample(){
				return new SimpleSample("provided");
			}
		});
		assertEquals("simple field = simple sample", obj.toString());
	}

	@Test
	public void testProvider() throws Exception {
		SimpleSample obj = new Diioc().create(SimpleSample.class, new Object(){
			Provider<SimpleSample> provider = new Provider<SimpleSample>(){
				@Override
				public SimpleSample get() {
					return new SimpleSample("provider");
				}
			};
		});
		assertEquals("provider", obj.toString());
	}

	@Test
	public void testProviderInjection() throws Exception {
		SimpleConstructorInjection obj = new Diioc().create(SimpleConstructorInjection.class, new Object(){
			Provider<SimpleSample> provider = new Provider<SimpleSample>(){
				@Override
				public SimpleSample get() {
					return new SimpleSample("provider");
				}
			};
		});
		assertEquals("simple constructor = provider", obj.toString());
	}

	@Test @Ignore
	public void testNamedInjection() throws Exception {
		NamedObject obj = new Diioc().create(NamedObject.class, new Object(){
			@Named("name") String name = "diioc";
		});
		assertEquals("name = diioc", obj.toString());
	}
}
