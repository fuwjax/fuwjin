package com.echovantage.sample;

import com.echovantage.arrest.AbstractResource;
import com.echovantage.arrest.Resource;

import java.util.Collections;
import java.util.List;

public class SampleApi extends AbstractResource {
	private final SampleWidgets widgets;

	public SampleApi() {
		this("/");
	}

	public SampleApi(final String prefix) {
		super(prefix, "api");
		widgets = new SampleWidgets(this);
	}

	public SampleWidgets widgets() {
		return widgets;
	}

	public SampleWidget widget(final Long wid) {
		return widgets.getWidget(wid);
	}

	protected List<Resource> children(){
		return Collections.singletonList(widgets);
	}
}
