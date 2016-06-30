package com.yuanluesoft.jeaf.base.model;

import java.io.Serializable;

import com.yuanluesoft.jeaf.util.CloneUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CloneableObject implements Serializable, Cloneable {

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		Object clonedObject = super.clone();
		CloneUtils.cloneProperties(this, clonedObject);
		return clonedObject;
	}
}