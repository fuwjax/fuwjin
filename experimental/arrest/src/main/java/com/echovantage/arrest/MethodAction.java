package com.echovantage.arrest;

import org.echovantage.wonton.Wonton;

import com.echovantage.arrest.except.ArrestException;

public interface MethodAction {
	MethodResult perform(final Wonton context, final Wonton content) throws ArrestException;
}
