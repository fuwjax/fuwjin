package org.fuwjin.maquis;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NFileEntity;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

import com.google.common.io.Resources;

public class HttpClassloaderHandler implements HttpAsyncRequestHandler<HttpRequest> {
	private static final ContentType TEXT_HTML = ContentType.create("text/html", "UTF-8");
	private final String docRoot;

	public HttpClassloaderHandler(final String docRoot) {
		this.docRoot = docRoot;
	}

	@Override
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request, final HttpContext context) {
		return new BasicAsyncRequestConsumer();
	}

	@Override
	public void handle(final HttpRequest request, final HttpAsyncExchange exchange, final HttpContext context) throws HttpException, IOException {
		final HttpResponse response = exchange.getResponse();
		final String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if(!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
			throw new MethodNotSupportedException(method + " method not supported");
		}
		String target = URLDecoder.decode(request.getRequestLine().getUri(), "UTF-8");
		target = target.replaceAll("\\?.*", "");
		final URL url = Resources.getResource(docRoot + target);
		try {
			final NFileEntity body = new NFileEntity(new File(url.toURI()), TEXT_HTML);
			response.setStatusCode(HttpStatus.SC_OK);
			response.setEntity(body);
		} catch(final URISyntaxException e) {
			final NStringEntity entity = new NStringEntity("<html><body><h1>File" + target + " not found</h1></body></html>", TEXT_HTML);
			response.setStatusCode(HttpStatus.SC_NOT_FOUND);
			response.setEntity(entity);
		}
		exchange.submitResponse(new BasicAsyncResponseProducer(response));
	}
}
