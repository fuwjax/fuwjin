package com.echovantage.arrest;

import static org.simpleframework.http.Protocol.ETAG;
import static org.simpleframework.http.Protocol.LAST_MODIFIED;

import java.io.IOException;
import java.util.Date;

import org.simpleframework.http.Response;

public interface MethodResult {
	default void setLastModified(final Response resp, final Date lastModified) {
		if(lastModified != null) {
			resp.setDate(LAST_MODIFIED, lastModified.getTime());
		}
	}

	default void setTag(final Response resp, final Long tag) {
		if(tag != null) {
			resp.setValue(ETAG, String.format("%016x", tag));
		}
	}

	public void apply(final Response resp) throws IOException;
}
