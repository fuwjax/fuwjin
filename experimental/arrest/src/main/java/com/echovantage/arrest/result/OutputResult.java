package com.echovantage.arrest.result;

import com.echovantage.arrest.ArrestContainer;
import com.echovantage.arrest.MethodResult;
import com.echovantage.arrest.Resource;
import com.echovantage.arrest.mime.MimeType;
import com.echovantage.arrest.mime.MimeTypeRegistry;
import org.echovantage.wonton.Wonton;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import static org.simpleframework.http.Protocol.CONTENT_MD5;
import static org.simpleframework.http.Protocol.LOCATION;
import static org.simpleframework.http.Status.OK;

public class OutputResult implements MethodResult {
	private final Resource resource;
	private final boolean nonCanonicalReference;
	private final Wonton result;
	private final MimeType mimeType;
	private ByteBuffer buffer;

	public OutputResult(final Resource resource, final Wonton context, final Wonton result) {
		this(resource, context, result, false);
	}

	public OutputResult(final Resource resource, final Wonton context, final Wonton result, final boolean isNonCanonicalTarget) {
		this.resource = resource;
		nonCanonicalReference = isNonCanonicalTarget;
		this.mimeType = MimeTypeRegistry.MIMETYPES.get(context.get("acceptType").asString());
		this.result = result;
	}

	@Override
	public void apply(final Response resp) throws IOException {
		if(nonCanonicalReference) {
			resp.setValue(LOCATION, resource.location());
		}
		setTag(resp, resource.tag());
		setLastModified(resp, resource.lastModified());
		resp.setContentType(mimeType.contentType());
		resp.setContentLength(buffer().remaining());
		resp.setValue(CONTENT_MD5, ArrestContainer.md5(buffer()));
		resp.setStatus(OK);
		try(WritableByteChannel channel = resp.getByteChannel()) {
			channel.write(buffer());
		}
	}

	protected ByteBuffer buffer() throws IOException {
		if(buffer == null) {
			buffer = mimeType.write(result);
		}
		buffer.rewind();
		return buffer;
	}

	public Wonton value() {
		return result;
	}
}
