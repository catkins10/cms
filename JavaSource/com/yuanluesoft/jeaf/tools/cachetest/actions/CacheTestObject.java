package com.yuanluesoft.jeaf.tools.cachetest.actions;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class CacheTestObject implements Serializable {
	private String value;

	public CacheTestObject(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "value:" + value + "," + super.toString();
	}
}
