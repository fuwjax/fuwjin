package com.echovantage.arrest;

import org.echovantage.wonton.Wonton;
import org.echovantage.wonton.WontonFactory;

import java.math.BigInteger;

import static com.echovantage.util.Charsets.UTF_8;
import static com.echovantage.util.MessageDigests.MD5;

public abstract class AbstractData {
	private Wonton wonton;

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null || !(obj instanceof AbstractData)) {
			return false;
		}
		AbstractData o = (AbstractData) obj;
		return getClass().equals(o.getClass()) && wonton().equals(o.wonton());
	}

	@Override
	public String toString() {
		return wonton().toString();
	}

	@Override
	public int hashCode() {
		return wonton().hashCode();
	}

	public long tag() {
		return new BigInteger(MD5.digest(wonton().toString().getBytes(UTF_8))).longValue();
	}

	public Wonton wonton(){
		if(wonton == null){
			wonton = toWonton(WontonFactory.FACTORY);
		}
		return wonton;
	}

	public abstract Wonton toWonton(WontonFactory factory);
}
