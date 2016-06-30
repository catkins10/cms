/*
 * Created on 2005-1-24
 *
 */
package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

import com.yuanluesoft.workflow.client.model.definition.Activity;

/**
 *
 * @author LinChuan
 * 某环节办理人所属角色
 *
 */
public class RoleActivity implements Serializable, WorkflowParticipant {
	private Activity activity;
	
	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		return "和环节" + (activity==null ? "":"[" +  activity.getName() + "]") + "办理人角色相同的人员"; 
	}
	/**
	 * ID
	 * @return
	 */
	public String getActivityId() {
		return activity.getId();
	}
	/**
	 * @return Returns the activity.
	 */
	public Activity getActivity() {
		return activity;
	}
	/**
	 * @param activity The activity to set.
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
