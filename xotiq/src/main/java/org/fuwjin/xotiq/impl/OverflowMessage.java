package org.fuwjin.xotiq.impl;

import java.util.Map;

import org.fuwjin.xotiq.Message;

public interface OverflowMessage extends Message {
	public Map<Object,Object> overflow();
}
