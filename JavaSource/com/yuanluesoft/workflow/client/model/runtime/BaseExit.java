/*
 * Created on 2006-4-17
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 流程出口基类
 * @author LinChuan
 * 
 */
public class BaseExit extends CloneableObject {
	private String id; //出口ID
	private String name; //出口名称
	private List workflowRoutes; //出口路由,由所经过的流程元素所对应的出口对象(ActivityExit,TransitionExit等)组成
	private boolean selected; //是否被选中
	
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the workflowRoutes.
	 */
	public List getWorkflowRoutes() {
		return workflowRoutes;
	}
	/**
	 * @param workflowRoutes The workflowRoutes to set.
	 */
	public void setWorkflowRoutes(List workflowRoutes) {
		this.workflowRoutes = workflowRoutes;
	}
	/**
	 * @return Returns the selected.
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected The selected to set.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}