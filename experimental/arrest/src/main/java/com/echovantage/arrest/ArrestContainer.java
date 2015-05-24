package com.echovantage.arrest;

import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.except.BadRequestException;
import com.echovantage.arrest.except.NotAcceptableException;
import com.echovantage.arrest.except.NotModifiedException;
import com.echovantage.arrest.except.PreconditionFailedException;
import com.echovantage.arrest.except.UnsupportedMediaTypeException;
import com.echovantage.arrest.except.VersionNotSupportedException;
import com.echovantage.arrest.mime.MimeType;
import com.echovantage.util.Streams;
import com.echovantage.util.http.QualityMap;
import org.echovantage.wonton.Wonton;
import org.echovantage.wonton.WontonFactory;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.echovantage.arrest.mime.MimeTypeRegistry.MIMETYPES;
import static com.echovantage.util.MessageDigests.MD5;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.echovantage.wonton.WontonFactory.FACTORY;
import static org.simpleframework.http.Protocol.ACCEPT;
import static org.simpleframework.http.Protocol.ACCEPT_CHARSET;
import static org.simpleframework.http.Protocol.CONTENT_MD5;
import static org.simpleframework.http.Protocol.CONTENT_TYPE;
import static org.simpleframework.http.Protocol.DATE;
import static org.simpleframework.http.Protocol.HOST;
import static org.simpleframework.http.Protocol.IF_MATCH;
import static org.simpleframework.http.Protocol.IF_MODIFIED_SINCE;
import static org.simpleframework.http.Protocol.IF_NONE_MATCH;
import static org.simpleframework.http.Protocol.IF_UNMODIFIED_SINCE;
import static org.simpleframework.http.Protocol.SERVER;
import static org.simpleframework.http.Protocol.VARY;

public class ArrestContainer implements Container {
	public static Connection connect(final int port, final Container container) throws IOException {
		final Server server = new ContainerServer(container);
		final Connection connection = new SocketConnection(server);
		connection.connect(new InetSocketAddress(port));
		return connection;
	}

	private static final Pattern PATH = Pattern.compile("[-\\w/.]+");
	private final String serverName;
	private final HostRegistry hosts;
	private final List<String> origins;
	private final Map<String, ContextHandler> preconditions = new LinkedHashMap<>();

	public ArrestContainer(final String name, final HostRegistry hosts, final String... origins) {
		serverName = name;
		this.hosts = hosts;
		this.origins = Arrays.asList(origins);
		preconditions.put("testLabel", ArrestContainer::applyLabel);
		preconditions.put("date", ArrestContainer::date);
		preconditions.put("server", this::server);
		preconditions.put("protocol", ArrestContainer::checkProtocol);
		preconditions.put("origin", this::checkOrigin);
		preconditions.put("acceptType", ArrestContainer::acceptType);
	}

	@Override
	public void handle(final Request req, final Response resp) {
		try {
			try {
				handleRest(req, resp);
			} catch(final IOException | RuntimeException e) {
				e.printStackTrace();
				resp.reset();
				resp.setStatus(Status.INTERNAL_SERVER_ERROR);
			} catch(final ArrestException e) {
				e.apply(resp);
			} finally {
				resp.close();
			}
		} catch(final IOException e) {
			e.printStackTrace();
		}
	}

	private void handleRest(final Request req, final Response resp) throws ArrestException, IOException {
			WontonFactory.MutableWonton context = FACTORY.newStruct();
			for(Map.Entry<String, ContextHandler> handler : preconditions.entrySet()) {
				context.with(handler.getKey(), handler.getValue().test(req, resp));
			}
			final Host host = hosts.get(req.getValue(HOST));
			final Resource resource = host.locate(path(req.getTarget()));
			Host.Method method = host.getMethod(req.getMethod());
			MethodAction action = resource.getAction(method);
			MimeType type = MIMETYPES.get(req.getValue(CONTENT_TYPE));
			if(type == null) {
				throw new UnsupportedMediaTypeException();
			}
			Wonton wonton = content(req, type);
			context.with("contentType", type.contentType());
			checkPreconditions(req, resource.tag(), resource.lastModified(), method.isSafe());
			MethodResult result = action.perform(context, wonton);
			result.apply(resp);
	}

	private static String acceptType(final Request req, final Response resp) throws ArrestException {
		QualityMap<MimeType> acceptTypes = QualityMap.parse(req.getValue(ACCEPT), MimeType::mimeType);
		QualityMap charsets = QualityMap.parse(req.getValue(ACCEPT_CHARSET), Charset::name).putIfAbsent("ISO-8859-1", 1);
		for(MimeType mimeType : Streams.over(MIMETYPES.types().stream().filter(acceptTypes::contains).sorted(acceptTypes::compare))) {
			for(Charset charset : Streams.over(mimeType.charsets().stream().filter(charsets::contains).sorted(charsets::compare))) {
				return mimeType.withCharset(charset).contentType();
			}
		}
		throw new NotAcceptableException();
	}

	private static void checkPreconditions(final Request req, final Long tag, final Date lastModified, final boolean safe) throws ArrestException {
		final String etag = tag == null ? null : String.format("%016x", tag);
		final Long time = lastModified == null ? null : lastModified.getTime();
		final Boolean ifNoneMatch = checkMatch(etag, req.getValue(IF_NONE_MATCH));
		final Boolean ifModifiedSince = checkModified(time, req.getDate(IF_MODIFIED_SINCE));
		if(TRUE.equals(ifNoneMatch) || TRUE.equals(ifModifiedSince)) {
			if(safe) {
				throw new NotModifiedException();
			}
			throw new PreconditionFailedException();
		}
		final Boolean ifMatch = checkMatch(etag, req.getValue(IF_MATCH));
		final Boolean ifUnmodifiedSince = checkModified(time, req.getDate(IF_UNMODIFIED_SINCE));
		if(FALSE.equals(ifMatch) || FALSE.equals(ifUnmodifiedSince)) {
			throw new PreconditionFailedException();
		}
	}

	private static Boolean checkModified(final Long lastModified, final long since) {
		if(lastModified == null || since == -1) {
			return null;
		}
		return lastModified <= since;
	}

	private static Boolean checkMatch(final String etag, final String match) {
		if(etag == null || match == null) {
			return null;
		}
		final List<String> matches = splitMatch(match);
		return matches.contains("*") || matches.contains(etag) || matches.contains("\"" + etag + "\"");
	}

	private static List<String> splitMatch(final String match) {
		if(match == null) {
			return null;
		}
		return Arrays.asList(match.trim().split("\\s*,\\s*"));
	}

	private boolean checkOrigin(final Request req, final Response resp) throws BadRequestException {
		String origin = req.getValue("Origin");
		if(origin != null) {
			if(!origins.contains(origin)) {
				throw new BadRequestException();
			}
			resp.setValue("Access-Control-Allow-Origin", origin);
			resp.setValue("Access-Control-Allow-Credentials", "true");
			resp.setValue(VARY, "Origin");
		}
		return origin != null;
	}

	private static Path path(final String target) throws BadRequestException {
		Matcher matcher = PATH.matcher(target);
		if(!matcher.find()) {
			throw new BadRequestException();
		}
		return Paths.get(matcher.group());
	}

	private static String applyLabel(final Request req, final Response resp) {
		final String label = req.getValue("X-Test-Label");
		if(label != null) {
			System.out.println(label);
			resp.setValue("X-Test-Label", label);
		}
		return label;
	}

	private static long date(final Request req, final Response resp) {
		long timestamp = timestamp(req);
		resp.setDate(DATE, timestamp);
		return timestamp;
	}

	private String server(final Request req, final Response resp) {
		resp.setValue(SERVER, serverName);
		return serverName;
	}

	private static long timestamp(final Request req) {
		final long requestTimestamp = req.getDate("X-Test-Date");
		if(requestTimestamp >= 0) {
			return requestTimestamp;
		}
		return req.getRequestTime();
	}

	private static String checkProtocol(final Request req, final Response resp) throws VersionNotSupportedException {
		if(req.getMajor() != 1 || req.getMinor() != 1) {
			throw new VersionNotSupportedException();
		}
		return req.getMajor() + "." + req.getMinor();
	}

	private static Wonton content(final Request req, final MimeType type) throws ArrestException, IOException {
		if(req.getContentLength() <= 0) {
			return null;
		}
		final ByteBuffer input = ByteBuffer.allocate((int) req.getContentLength());
		while(input.hasRemaining()) {
			req.getByteChannel().read(input);
		}
		input.flip();
		final String md5 = req.getValue(CONTENT_MD5);
		if(md5 != null && !md5.equals(md5(input))) {
			throw new BadRequestException();
		}
		if(type == null) {
			throw new UnsupportedMediaTypeException();
		}
		try {
			return type.read(input);
		} catch(ParseException e) {
			throw new BadRequestException();
		}
	}

	public static String md5(final ByteBuffer input) {
		return Base64.getEncoder().encodeToString(MD5.digest(input.array()));
	}
}
