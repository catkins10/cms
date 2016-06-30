/*
 * Created on 2005-1-24
 *
 */
package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.workflow.client.model.definition.Activity;

/**
 *
 * @author LinChuan
 * 角色中特定人员
 *
 */
public class RoleFilter extends Element implements Serializable, WorkflowParticipant {
	private String filter; //过滤方式:creator/创建者所在部门,current/当前办理人所在部门,activity/环节办理人所在部门,department/指定部门
	private Activity activity; //过滤方式为activity时有效
	private String departmentId; //过滤方式为Department时有效
	private String departmentName; //过滤方式为Department时有效
	
	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		String prefix = "";
		if(filter.equals("creator")) {
			prefix = "创建者所在部门或单位";
		}
		else if(filter.equals("current")) {
			prefix = "当前办理人所在部门或单位";
		}
		else if(filter.equals("activity")) {
			prefix = "环节" + (activity==null ? "":"[" + activity.getName() + "]") + "办理人所在部门或单位";
		}
		else if(filter.equals("department")) {
			prefix = "部门[" + departmentName + "]";
		}
		return prefix + "的[" + getName() + "]"; 
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
	/**
	 * @return Returns the departmentId.
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId The departmentId to set.
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return Returns the departmentName.
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName The departmentName to set.
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * @return Returns the filter.
	 */
	public String getFilter() {
		return filter;
	}
	/**
	 * @param filter The filter to set.
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
