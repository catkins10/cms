/*
 * Created on 2006-4-21
 *
 */
package com.yuanluesoft.workflow.client.model.user.visitor;

import java.io.Serializable;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Visitor extends Element implements Serializable {
	private Timestamp created; //添加时间
	
	public Visitor(String id, String name) {
		super(id, name);
	}
	
	/**
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
}
