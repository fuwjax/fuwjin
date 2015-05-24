package com.echovantage.util.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QualityMap<V> {
	private static final Pattern QUALITY = Pattern.compile("\\s*;\\s*q\\s*=\\s*([0-9.]+)");

	public static <V> QualityMap<V> parse(final String accept, final Function<V, String> namer) {
		return new QualityMap<>(mapOf(accept), namer);
	}

	private static Map<String, Double> mapOf(final String accept) {
		String value = accept == null ? "" : accept.trim();
		if("".equals(value)) {
			return Collections.emptyMap();
		}
		final String[] split = value.split("\\s*,\\s*");
		if(split.length == 1) {
			double quality = 1;
			final Matcher matcher = QUALITY.matcher(value);
			if(matcher.find()) {
				quality = Double.valueOf(matcher.group(1));
				value = matcher.replaceAll("");
			}
			return Collections.singletonMap(value, quality);
		}
		final Map<String, Double> types = new HashMap<>();
		for(final String pair : split) {
			value = pair;
			double quality = 1;
			final Matcher matcher = QUALITY.matcher(pair);
			if(matcher.find()) {
				quality = Double.valueOf(matcher.group(1));
				value = matcher.replaceAll("");
			}
			types.put(value, quality);
		}
		return types;
	}

	private final Map<String, Double> map;
	private final Function<V, String> namer;

	private QualityMap(final Map<String, Double> map, final Function<V, String> namer) {
		this.map = map;
		this.namer = namer;
	}

	public double get(final String key) {
		Double quality = map.get(key);
		if(quality == null) {
			if(key.contains("/")) {
				quality = map.get(key.substring(0, key.indexOf('/')) + "/*");
				if(quality == null) {
					quality = map.get("*/*");
				}
			} else {
				quality = map.get("*");
			}
		}
		return quality == null ? 0.0 : quality;
	}

	public double get(final V value) {
		return get(namer.apply(value));
	}

	public boolean contains(final String key) {
		if(isEmpty()) {
			return true;
		}
		Double quality = map.get(key);
		if(quality == null) {
			if(key.contains("/")) {
				quality = map.get(key.substring(0, key.indexOf('/')) + "/*");
				if(quality == null) {
					quality = map.get("*/*");
				}
			} else {
				quality = map.get("*");
			}
		}
		return quality != null;
	}

	public boolean contains(final V value) {
		return contains(namer.apply(value));
	}

	public int compare(final V v1, final V v2) {
		return Double.compare(get(v1), get(v2));
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		String delim = "";
		for(final Map.Entry<String, Double> entry : map.entrySet()) {
			builder.append(delim).append(entry.getKey()).append(";q=").append(entry.getValue());
			delim = ", ";
		}
		return builder.toString();
	}

	public QualityMap putIfAbsent(final String key, final double weight) {
		if(contains(key)) {
			return this;
		}
		Map<String, Double> newMap = new HashMap<>(map);
		newMap.put(key, weight);
		return new QualityMap(newMap, namer);
	}
}
