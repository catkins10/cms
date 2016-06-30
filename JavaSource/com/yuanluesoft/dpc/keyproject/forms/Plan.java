package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectPlan;

/**
 * 
 * @author linchuan
 *
 */
public class Plan extends Project {
	private KeyProjectPlan projectPlan = new KeyProjectPlan();

	/**
	 * @return the projectPlan
	 */
	public KeyProjectPlan getProjectPlan() {
		return projectPlan;
	}

	/**
	 * @param projectPlan the projectPlan to set
	 */
	public void setProjectPlan(KeyProjectPlan projectPlan) {
		this.projectPlan = projectPlan;
	}
}