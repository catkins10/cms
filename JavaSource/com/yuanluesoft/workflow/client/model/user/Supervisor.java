package com.yuanluesoft.workflow.client.model.user;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.workflow.client.model.definition.Activity;

/**
 * 分管领导
 * @author linchuan
 *
 */
public class Supervisor  extends Element implements Serializable, WorkflowParticipant {
	private String filter; //过滤方式:creatorDepartment/创建者所在部门,currentDepartment/当前办理人所在部门,activityDepartment/环节办理人所在部门,department/指定部门,creator/创建者,current/当前办理人,activity/环节办理人
	private Activity activity; //过滤方式为activityDepartment/activity时有效
	private String departmentId; //过滤方式为Department时有效
	private String departmentName; //过滤方式为Department时有效
	
	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		String prefix = "";
		if(filter.equals("creatorDepartment")) {
			prefix = "创建者所在部门或单位";
		}
		else if(filter.equals("currentDepartment")) {
			prefix = "当前办理人所在部门或单位";
		}
		else if(filter.equals("activityDepartment")) {
			prefix = "环节" + (activity==null ? "":"[" + activity.getName() + "]") + "办理人所在部门或单位";
		}
		else if(filter.equals("department")) {
			prefix = "部门[" + departmentName + "]";
		}
		else if(filter.equals("creator")) {
			prefix = "创建者";
		}
		else if(filter.equals("current")) {
			prefix = "当前办理人";
		}
		else if(filter.equals("activity")) {
			prefix = "环节" + (activity==null ? "":"[" + activity.getName() + "]") + "办理人";
		}
		return prefix + "的分管领导"; 
	}
	
	/**
	 * ID
	 * @return
	 */
	public String getActivityId() {
		return activity.getId();
	}

	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
}