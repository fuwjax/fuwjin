package com.echovantage.sample;

import com.echovantage.arrest.AbstractResource;
import com.echovantage.arrest.MethodResult;
import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.except.LengthRequiredException;
import com.echovantage.arrest.result.ResourceResult;
import org.echovantage.wonton.Wonton;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SampleWidgets extends AbstractResource {
	private final AtomicLong idGen = new AtomicLong();
	private final Map<Long, SampleWidget> widgets = new HashMap<>();

	public SampleWidgets(final AbstractResource parent) {
		super(parent, "widgets", "widget");
	}

	@Override
	protected MethodResult post(final Wonton context, final Wonton content) throws ArrestException {
		SampleData value = new SampleData(context, content);
		SampleWidget widget = createWidget(value);
		return new ResourceResult(widget, true);
	}

	@Override
	protected MethodResult get(final Wonton context, final Wonton content) throws ArrestException {
		if(content == null){
			throw new LengthRequiredException();
		}
		return new ResourceResult(locate(content.get("id").asString()));
	}

	public SampleWidget createWidget(final SampleData value) {
		final long id = idGen.incrementAndGet();
		final SampleWidget r = new SampleWidget(this, id, value);
		widgets.put(id, r);
		return r;
	}

	public Collection<SampleWidget> getWidgets() {
		return widgets.values();
	}

	@Override
	public SampleWidget locate(final String id) {
		return getWidget(Long.valueOf(id));
	}

	public SampleWidget getWidget(final Long id) {
		return widgets.get(id);
	}

	@Override
	public Date lastModified() {
		return lastModified(widgets.values());
	}
}
