/*
 * Created on 2005-1-24
 *
 */
package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

/**
 *
 * @author LinChuan
 * 当前办理人所属角色
 *
 */
public class RoleCurrent implements Serializable, WorkflowParticipant {
	
	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		return "和当前办理人角色相同的人员"; 
	}
}
