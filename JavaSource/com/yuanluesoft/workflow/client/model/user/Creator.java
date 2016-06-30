/*
 * Created on 2005-1-24
 *
 */
package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

/**
 *
 * @author LinChuan
 * 创建者
 *
 */
public class Creator implements Serializable, WorkflowParticipant {

	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		return "创建者"; 
	}
}
