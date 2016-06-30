/*
 * Created on 2005-4-26
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import com.yuanluesoft.workflow.client.model.instance.WorkItem;
import com.yuanluesoft.workflow.client.model.wapi.ActivityInstance;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowUndo {
	private ActivityInstance fromActivityInstance; //源活动实例
	private WorkItem toWorkItem; //目标工作项
	
	/**
	 * 获取标题
	 * @return
	 */
	public String getTitle() {
		if(fromActivityInstance==toWorkItem.getActivityInstance()) {
			return "取回重办(" + toWorkItem.getActivityInstance().getName() + ")";
		}
		else {
			return "从" + fromActivityInstance.getName() + "取回(" + toWorkItem.getActivityInstance().getName() + ")";
		}
	}
	/**
	 * 获取id
	 * @return
	 */
	public String getId() {
		return fromActivityInstance.getId() + "-" + toWorkItem.getId();
	}
	/**
	 * @return Returns the toWorkItem.
	 */
	public WorkItem getToWorkItem() {
		return toWorkItem;
	}
	/**
	 * @param toWorkItem The toWorkItem to set.
	 */
	public void setToWorkItem(WorkItem toWorkItem) {
		this.toWorkItem = toWorkItem;
	}
	/**
	 * @return Returns the fromActivityInstance.
	 */
	public ActivityInstance getFromActivityInstance() {
		return fromActivityInstance;
	}
	/**
	 * @param fromActivityInstance The fromActivityInstance to set.
	 */
	public void setFromActivityInstance(ActivityInstance fromActivityInstance) {
		this.fromActivityInstance = fromActivityInstance;
	}
}
