/*
 * Created on 2005-2-7
 *
 */
package com.yuanluesoft.workflow.client.model.wapi;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class ProcessDefinitionState implements Cloneable, Serializable {
	public static final int ENABLE = 0;
	public static final int DISABLE = 1;
	public static final int UPDATE = 2; //已被新流程过程替代,等到所有过程实例完成后自动销毁
	public static final int DELETE = 3; //已删除,等到所有过程实例完成后自动销毁
	
	private int state;
	
	/**
	 * @return Returns the state.
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(int state) {
		this.state = state;
	}
}
