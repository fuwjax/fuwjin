package com.echovantage.test;

import static com.echovantage.arrest.ArrestContainer.connect;

import org.echovantage.gild.Gild;
import org.echovantage.gild.proxy.http.HttpClientProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.simpleframework.transport.connect.Connection;

import com.echovantage.arrest.ArrestContainer;
import com.echovantage.arrest.Host;
import com.echovantage.arrest.HostRegistry;
import com.echovantage.sample.SampleApi;

public class RestTest {
	private static final int PORT = 8080;
	private final HttpClientProxy client = new HttpClientProxy(PORT);
	@Rule
	public Gild gild = new Gild().with("client", client);
	private Connection server;

	@Before
	public void setup() throws Exception {
		server = connect(PORT,
		      new ArrestContainer("test",
		            new HostRegistry()
		                  .register("localhost",
		                        new Host().register(new SampleApi()))));
	}

	@After
	public void teardown() throws Exception {
		server.close();
	}

	@Test
	public void testBadRequest() {
		client.send();
	}

	@Test
	public void testFlowTest() {
		client.send();
	}
}
