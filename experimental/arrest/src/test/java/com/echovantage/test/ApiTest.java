package com.echovantage.test;

import com.echovantage.arrest.Host;
import com.echovantage.arrest.Resource;
import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.result.NoResult;
import com.echovantage.arrest.result.OutputResult;
import com.echovantage.arrest.result.ResourceResult;
import com.echovantage.sample.SampleApi;
import com.echovantage.sample.SampleData;
import com.echovantage.sample.SampleWidget;
import com.echovantage.sample.SampleWidgets;
import org.echovantage.wonton.Wonton;
import org.echovantage.wonton.WontonFactory.MutableWonton;
import org.junit.Test;

import java.util.Date;

import static java.util.Collections.*;
import static org.echovantage.wonton.WontonFactory.FACTORY;
import static org.junit.Assert.*;

public class ApiTest {
	@Test
	public void testDirectApi() {
		final SampleApi api = new SampleApi();
		final SampleWidgets container = api.widgets();
		final SampleWidget r = container.createWidget(new SampleData("test", new Date()));
		assertEquals("test", r.getValue().value());
		final SampleWidget r2 = container.getWidget(r.id());
		assertEquals("test", r2.getValue().value());
		final SampleWidget r3 = api.widget(r.id());
		assertEquals("test", r3.getValue().value());
		r.setValue(new SampleData("hi", new Date()));
		assertEquals("hi", r.getValue().value());
	}

	@Test
	public void testGeneratedApi() throws ArrestException {
		MutableWonton context = FACTORY.newStruct();
		context.with("acceptType", "application/json");
		context.with("date", System.currentTimeMillis());
		final Host host = new Host().register(new SampleApi());
		final Resource container = host.locate("/api/widgets");
		final Resource r = ((ResourceResult) container.getAction(Host.Method.POST).perform(context, FACTORY.wontonOf(singletonMap("value", "test")))).resource();
		assertEquals("test", ((OutputResult) r.getAction(Host.Method.GET).perform(context, Wonton.NULL)).value().get("value").value());
		// no parallel to testApi.r2
		final Resource r3 = host.locate(r.location());
		assertEquals("test", ((OutputResult) r3.getAction(Host.Method.GET).perform(context, Wonton.NULL)).value().get("value").value());
		assertEquals(r.getAction(Host.Method.PUT).perform(context, FACTORY.wontonOf(singletonMap("value", "hi"))).getClass(), NoResult.class);
		assertEquals("hi", ((OutputResult) r.getAction(Host.Method.GET).perform(context, Wonton.NULL)).value().get("value").value());
	}
}
