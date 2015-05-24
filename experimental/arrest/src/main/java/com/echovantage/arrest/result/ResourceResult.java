package com.echovantage.arrest.result;

import static org.simpleframework.http.Protocol.LOCATION;
import static org.simpleframework.http.Status.CREATED;
import static org.simpleframework.http.Status.NO_CONTENT;

import org.simpleframework.http.Response;

import com.echovantage.arrest.MethodResult;
import com.echovantage.arrest.Resource;

public class ResourceResult implements MethodResult {
	private final Resource result;
	private final boolean isCreated;

	public ResourceResult(final Resource result) {
		this(result, false);
	}

	public ResourceResult(final Resource result, final boolean isCreated) {
		this.result = result;
		this.isCreated = isCreated;
	}

	@Override
	public void apply(final Response resp) {
		resp.setValue(LOCATION, result.location());
		setTag(resp, result.tag());
		setLastModified(resp, result.lastModified());
		resp.setStatus(isCreated ? CREATED : NO_CONTENT);
	}

	public Resource resource() {
		return result;
	}
}
