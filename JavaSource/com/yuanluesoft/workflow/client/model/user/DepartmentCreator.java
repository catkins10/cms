/*
 * Created on 2005-1-24
 *
 */
package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

/**
 *
 * @author LinChuan
 * 创建者所在部门
 *
 */
public class DepartmentCreator implements Serializable, WorkflowParticipant {
	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		return "和创建者相同部门的人员"; 
	}
}
