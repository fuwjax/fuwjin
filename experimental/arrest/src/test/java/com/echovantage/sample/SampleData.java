package com.echovantage.sample;

import com.echovantage.arrest.AbstractData;
import org.echovantage.wonton.Wonton;
import org.echovantage.wonton.WontonFactory;

import java.util.Date;

public class SampleData extends AbstractData {
	public final String value;
	public Date timestamp;

	public SampleData(final String value, final Date timestamp) {
		this.value = value;
		this.timestamp = timestamp;
	}

	public SampleData(final Wonton context, final Wonton content) {
		this(content.get("value").asString(),
		      new Date(context.get("date").asLong()));
	}

	public String value() {
		return value;
	}

	public Wonton toWonton(WontonFactory factory) {
		return factory.newStruct().with("value", value);
	}
}
