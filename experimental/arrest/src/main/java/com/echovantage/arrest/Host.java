package com.echovantage.arrest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.except.NotFoundException;
import com.echovantage.arrest.except.NotImplementedException;
import org.echovantage.wonton.Wonton;

public class Host {
	public enum Method {
		DELETE(false),
		GET(true),
		HEAD(true) {
			@Override
			public boolean hasHttpMethod(final Class<?> type) {
				return GET.hasHttpMethod(type);
			}
		},
		OPTIONS(true) {
			@Override
			public boolean hasHttpMethod(final Class<?> type) {
				return true;
			}
		},
		POST(false),
		PUT(false)
		;
		private boolean isSafe;

		private Method(final boolean isSafe) {
			this.isSafe = isSafe;
		}

		public boolean isSafe() {
			return isSafe;
		}

		public boolean hasHttpMethod(final Class<?> type) {
			try {
				type.getDeclaredMethod(toString().toLowerCase(), Wonton.class, Wonton.class);
				return true;
			} catch(NoSuchMethodException e) {
				// continue
			}
			return false;
		}
	}

	private final Map<String, Resource> resources = new HashMap<>();
	private final Path prefix;

	public Host(final Path prefix) {
		this.prefix = prefix;
	}

	public Host() {
		this(null);
	}

	public Resource locate(final Path path) throws ArrestException {
		if(prefix != null && !path.startsWith(prefix)) {
			throw new NotFoundException();
		}
		Path p = prefix == null ? path : prefix.relativize(path);
		if(p.getNameCount() == 0) {
			throw new NotFoundException();
		}
		Resource r = resources.get(p.getName(0).toString());
		if(r == null) {
			throw new NotFoundException();
		}
		for(int i = 1; i < p.getNameCount(); i++) {
			r = r.locate(p.getName(i).toString());
			if(r == null) {
				throw new NotFoundException();
			}
		}
		return r;
	}

	public Method getMethod(String name) throws ArrestException{
		try {
			return Method.valueOf(name.toUpperCase());
		}catch(Exception e){
			throw new NotImplementedException(Arrays.asList(Method.values()));
		}
	}

	public Host register(final Resource resource) {
		for(String name : resource.names()) {
			resources.put(name, resource);
		}
		return this;
	}

	public Resource locate(final String path) throws ArrestException {
		return locate(Paths.get(path));
	}
}
