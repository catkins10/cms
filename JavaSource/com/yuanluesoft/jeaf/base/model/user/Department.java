/*
 * Created on 2005-1-1
 *
 */
package com.yuanluesoft.jeaf.base.model.user;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Department extends Element implements Serializable {

	public Department() {
		super();
	}

	public Department(String id, String name) {
		super(id, name);
	}
}