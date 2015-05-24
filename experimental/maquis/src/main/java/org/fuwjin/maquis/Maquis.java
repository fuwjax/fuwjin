package org.fuwjin.maquis;

import java.net.InetSocketAddress;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.impl.nio.DefaultNHttpServerConnection;
import org.apache.http.impl.nio.DefaultNHttpServerConnectionFactory;
import org.apache.http.impl.nio.SSLNHttpServerConnectionFactory;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.protocol.UriHttpAsyncRequestHandlerMapper;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import com.google.common.io.Resources;

public class Maquis {
	public static void main(final String... args) throws Exception {
		final MaquisConfig config = MaquisConfig.load(args[0]);
		final HttpProcessor httpproc = HttpProcessorBuilder.create()
		      .add(new ResponseDate())
		      .add(new ResponseServer(config.originServer))
		      .add(new ResponseContent())
		      .add(new ResponseConnControl()).build();
		final UriHttpAsyncRequestHandlerMapper reqistry = new UriHttpAsyncRequestHandlerMapper();
		reqistry.register("*", new HttpClassloaderHandler(config.webRoot));
		final HttpAsyncService protocolHandler = new HttpAsyncService(httpproc, reqistry);
		final IOEventDispatch ioEventDispatch = new DefaultHttpServerIODispatch(protocolHandler, factory(config));
		final IOReactorConfig reactorConfig = IOReactorConfig.custom()
		      .setIoThreadCount(config.threads)
		      .setSoTimeout(config.soTimeout)
		      .setConnectTimeout(config.connectTimeout)
		      .build();
		final ListeningIOReactor ioReactor = new DefaultListeningIOReactor(reactorConfig);
		ioReactor.listen(new InetSocketAddress(config.port));
		ioReactor.execute(ioEventDispatch);
	}

	private static NHttpConnectionFactory<DefaultNHttpServerConnection> factory(final MaquisConfig config) throws Exception {
		if(!config.useSsl) {
			return new DefaultNHttpServerConnectionFactory(ConnectionConfig.DEFAULT);
		}
		final URL url = Resources.getResource(config.keystore);
		final KeyStore keystore = KeyStore.getInstance("jks");
		keystore.load(url.openStream(), config.keystoreSecret.toCharArray());
		final KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmfactory.init(keystore, config.keystoreSecret.toCharArray());
		final KeyManager[] keymanagers = kmfactory.getKeyManagers();
		final SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(keymanagers, null, null);
		return new SSLNHttpServerConnectionFactory(sslcontext, null, ConnectionConfig.DEFAULT);
	}
}
