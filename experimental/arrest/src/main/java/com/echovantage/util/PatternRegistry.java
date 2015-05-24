package com.echovantage.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternRegistry<T> {
	public interface Factory<T, V> {
		V create(T value, Matcher matcher);
	}

	private final Factory<T, T> IDENTITY = new Factory<T, T>() {
		@Override
		public T create(final T value, final Matcher matcher) {
			return value;
		}
	};
	private final Map<Pattern, T> registry = new LinkedHashMap<>();

	public void register(final String pattern, final T target) {
		registry.put(Pattern.compile(pattern), target);
	}

	public <V> V get(final String request, final Factory<T, V> factory) {
		for(final Map.Entry<Pattern, T> entry : registry.entrySet()) {
			final Matcher matcher = entry.getKey().matcher(request);
			if(matcher.matches()) {
				return factory.create(entry.getValue(), matcher);
			}
		}
		return null;
	}

	public T get(final String request) {
		return get(request, IDENTITY);
	}
}
