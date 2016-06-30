/*
 * Created on 2006-5-26
 *
 */
package com.yuanluesoft.eai.server.model;

import java.io.Serializable;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Group extends Element implements Cloneable, Serializable {
	 private List children; //分组下的项目列表

	/**
	 * @return the children
	 */
	public List getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List children) {
		this.children = children;
	}
}