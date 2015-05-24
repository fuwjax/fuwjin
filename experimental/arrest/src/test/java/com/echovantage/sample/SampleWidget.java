package com.echovantage.sample;

import com.echovantage.arrest.AbstractResource;
import com.echovantage.arrest.MethodResult;
import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.result.NoResult;
import com.echovantage.arrest.result.OutputResult;
import org.echovantage.wonton.Wonton;

import java.util.Date;

public class SampleWidget extends AbstractResource {
	private SampleData value;

	public SampleWidget(final AbstractResource parent, final long id, final SampleData value) {
		super(parent, id);
		setValue(value);
	}

	public Long id() {
		return (Long) super.id();
	}

	@Override
	protected MethodResult get(final Wonton context, final Wonton content) throws ArrestException {
		return new OutputResult(this, context, value.wonton());
	}

	@Override
	protected MethodResult put(final Wonton context, final Wonton content) throws ArrestException {
		setValue(new SampleData(context, content));
		return new NoResult();
	}

	public SampleData getValue() {
		return value;
	}

	public String getStringValue() {
		return value.value;
	}

	public void setValue(final String value, final Date timestamp) {
		setValue(new SampleData(value, timestamp));
	}

	public void setValue(final SampleData value) {
		this.value = value;
	}

	@Override
	public Date lastModified() {
		return value.timestamp;
	}

	@Override
	public Long tag() {
		return value.tag();
	}
}
