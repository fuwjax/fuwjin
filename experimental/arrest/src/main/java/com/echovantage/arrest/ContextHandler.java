package com.echovantage.arrest;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.echovantage.arrest.except.ArrestException;

public interface ContextHandler {
	Object test(Request req, Response resp) throws ArrestException;
}
