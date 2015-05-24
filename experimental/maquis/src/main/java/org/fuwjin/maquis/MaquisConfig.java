package org.fuwjin.maquis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.google.common.io.Resources;

public class MaquisConfig {
	public int port = 8080;
	public String originServer = "Test/1.1";
	public String webRoot = "www/";
	public String keystore = "keystore";
	public String keystoreSecret = "secret";
	public boolean useSsl = false;
	public int connectTimeout = 3000;
	public int soTimeout = 3000;
	public int threads = 1;

	public static MaquisConfig load(final String path) throws IOException {
		final MaquisConfig config = new MaquisConfig();
		for(final Map.Entry<Object, Object> prop : loadProps(path).entrySet()) {
			try {
				final Field field = MaquisConfig.class.getDeclaredField(String.valueOf(prop.getKey()));
				field.setAccessible(true);
				field.set(config, toType(String.valueOf(prop.getValue()), field.getType()));
			} catch(final NoSuchFieldException e) {
				// continue
			} catch(final Exception e) {
				throw new RuntimeException("could not set config field " + prop.getKey() + " = " + prop.getValue(), e);
			}
		}
		return config;
	}

	private static Object toType(final String value, final Class<?> type) {
		if(String.class.equals(type)) {
			return value;
		}
		if(boolean.class.equals(type)) {
			return "true".equalsIgnoreCase(value);
		}
		if(int.class.equals(type)) {
			return Integer.valueOf(value);
		}
		throw new UnsupportedOperationException("Unknown type: " + type);
	}

	private static Properties loadProps(final String path) throws IOException {
		final Properties props = new Properties();
		try {
			props.load(new URL(path).openStream());
		} catch(final MalformedURLException e) {
			try {
				props.load(new FileInputStream(path));
			} catch(final FileNotFoundException ex) {
				props.load(Resources.getResource(path).openStream());
			}
		}
		return props;
	}
}
