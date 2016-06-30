/*
 * Created on 2005-2-17
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 *
 * @author LinChuan
 * 流程出口
 *
 */
public class WorkflowExit extends CloneableObject {
	private List exits; //出口集
	
	/**
	 * 添加出口
	 * @param exit
	 */
	public void addExit(BaseExit exit) {
		if(exits==null) {
			exits = new ArrayList();
		}
		exits.add(exit);
	}
	/**
	 * @return Returns the exits.
	 */
	public List getExits() {
		return exits;
	}
	/**
	 * @param exits The exits to set.
	 */
	public void setExits(List exits) {
		this.exits = exits;
	}
}