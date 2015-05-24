package com.echovantage.arrest;

import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.except.MethodNotAllowedException;
import com.echovantage.arrest.result.OptionResult;
import org.echovantage.wonton.Wonton;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractResource implements Resource {
	protected static long tag(final Iterable<? extends Resource> metas) {
		long tag = 17;
		for(final Resource meta : metas) {
			tag = tag * 31 + meta.tag();
		}
		return tag;
	}

	protected static Date lastModified(final Resource... resources) {
		return lastModified(Arrays.asList(resources));
	}

	protected static long tag(final Resource... resources) {
		return tag(Arrays.asList(resources));
	}

	protected static Date lastModified(final Iterable<? extends Resource> metas) {
		Date lastModified = null;
		for(final Resource meta : metas) {
			final Date lm = meta.lastModified();
			lastModified = lastModified == null ? lm : lm == null ? lastModified : lastModified.compareTo(lm) < 0 ? lm : lastModified;
		}
		return lastModified;
	}

	protected static long tag(final String value) {
		long tag = 17;
		for(final char c : value.toCharArray()) {
			tag = tag * 31 + c;
		}
		return tag;
	}

	protected static long tag(final long... values) {
		long tag = 13;
		for(final long v : values) {
			tag = tag * 37 + v;
		}
		return tag;
	}

	protected static long tag(final Object... objects) {
		long tag = 13;
		for(final Object o : objects) {
			tag = tag * 37 + tag(o);
		}
		return tag;
	}

	protected static long tag(final Object obj) {
		if(obj == null) {
			return 0;
		}
		if(obj instanceof Resource) {
			return ((Resource) obj).tag();
		}
		if(obj instanceof AbstractData) {
			return ((AbstractData) obj).tag();
		}
		if(obj instanceof Object[]) {
			return tag((Object[]) obj);
		}
		if(obj instanceof List) {
			long tag = 13;
			for(final Object o : (List<?>) obj) {
				tag = tag * 37 + tag(o);
			}
			return tag;
		}
		if(obj instanceof Map) {
			long tag = 31;
			for(final Map.Entry<?, ?> e : ((Map<?, ?>) obj).entrySet()) {
				tag += tag(e.getKey()) ^ tag(e.getValue());
			}
			return tag;
		}
		if(obj instanceof Number) {
			return ((Number) obj).longValue();
		}
		return tag(obj.toString());
	}

	private static final Map<Class<?>, Collection<String>> resourceOptions = new HashMap<>();
	private final Collection<String> options;
	private final String location;
	private final String containerLocation;
	private final Object id;
	private final Collection<String> names;

	public AbstractResource(final Resource parent, final Object id) {
		this(parent, id, id);
	}

	public AbstractResource(final String parent, final Object id) {
		this(parent, id, id);
	}

	public AbstractResource(final Resource parent, final Object id, final Object containerId) {
		this(parent.containerLocation(), id, containerId);
	}

	public AbstractResource(final String parent, final Object id, final Object containerId) {
		if(Objects.equals(id, containerId) || containerId == null) {
			names = Collections.singleton(id.toString());
		} else {
			names = Arrays.asList(id.toString(), containerId.toString());
		}
		this.id = id;
		this.location = parent + id;
		this.containerLocation = parent + containerId + "/";
		options = options(getClass());
	}

	private static Collection<String> options(final Class<?> type) {
		Collection<String> opts = resourceOptions.get(type);
		if(opts == null) {
			opts = Arrays.asList(Host.Method.values()).stream().filter(m -> m.hasHttpMethod(type)).map(Host.Method::toString).sorted().collect(Collectors.toList());
			resourceOptions.put(type, opts);
		}
		return opts;
	}

	protected Collection<Resource> children(){
		return Collections.emptySet();
	}

	public Object id(){
		return id;
	}

	@Override
	public Resource locate(String name) {
		for(Resource child: children()) {
			if (child.names().contains(name)) {
				return child;
			}
		}
		return null;
	}

	@Override
	public String location() {
		return location;
	}

	@Override
	public String containerLocation() {
		return containerLocation;
	}

	@Override
	public Collection<String> names() {
		return names;
	}

	@Override
	public Long tag() {
		return null;
	}

	@Override
	public Date lastModified() {
		return null;
	}

	@Override
	public Collection<String> options() {
		return options;
	}

	@Override
	public MethodAction getAction(final Host.Method method) throws MethodNotAllowedException {
		switch(method) {
			case DELETE:
				return this::delete;
			case GET:
				return this::get;
			case HEAD:
				return this::head;
			case OPTIONS:
				return this::options;
			case POST:
				return this::post;
			case PUT:
				return this::put;
			default:
				throw new MethodNotAllowedException(this);
		}
	}

	private MethodResult head(final Wonton context, final Wonton content) throws ArrestException {
		MethodResult result = get(context, content);
		return new MethodResult() {
			@Override
			public void apply(final Response resp) throws IOException {
				result.apply(new HeadResponse(resp));
			}
		};
	}

	protected MethodResult get(final Wonton context, final Wonton content) throws ArrestException {
		throw new MethodNotAllowedException(this);
	}

	protected MethodResult post(final Wonton context, final Wonton content) throws ArrestException {
		throw new MethodNotAllowedException(this);
	}

	protected MethodResult put(final Wonton context, final Wonton content) throws ArrestException {
		throw new MethodNotAllowedException(this);
	}

	protected MethodResult delete(final Wonton context, final Wonton content) throws ArrestException {
		throw new MethodNotAllowedException(this);
	}

	private MethodResult options(final Wonton context, final Wonton content) throws ArrestException {
		return new OptionResult(context, this);
	}

	@Override
	public boolean equals(final Object obj) {
		try {
			AbstractResource o = (AbstractResource) obj;
			return Objects.equals(location(), o.location()) && getClass().equals(o.getClass());
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return location().hashCode();
	}

	@Override
	public String toString() {
		return location();
	}
}
