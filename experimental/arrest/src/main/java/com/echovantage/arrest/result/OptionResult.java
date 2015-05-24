package com.echovantage.arrest.result;

import com.echovantage.arrest.MethodResult;
import com.echovantage.arrest.Resource;
import com.echovantage.util.Streams;
import org.echovantage.wonton.Wonton;
import org.simpleframework.http.Response;

import static org.simpleframework.http.Protocol.ALLOW;
import static org.simpleframework.http.Status.NO_CONTENT;

public class OptionResult implements MethodResult {
	private final Resource resource;
	private final Wonton context;

	public OptionResult(final Wonton context, final Resource resource) {
		this.context = context;
		this.resource = resource;
	}

	@Override
	public void apply(final Response resp) {
		resp.setValue(ALLOW, Streams.join(",", resource.options()));
		if(context.get("origin").value() == Boolean.TRUE) {
			resp.setValue("Access-Control-Allow-Methods", Streams.join(",", resource.options()));
			resp.setValue("Access-Control-Allow-Headers", Streams.join(",", "Content-Type", "Content-Schema", "Accept-Schema"));
		}
		resp.setStatus(NO_CONTENT);
	}
}
