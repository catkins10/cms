/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * @author Administrator
 *
 *
 */
public class SchoolJobs extends ActionForm {
	private String jobName;
	private int priority;
	
	/**
	 * @return Returns the jobName.
	 */
	public String getJobName() {
		return jobName;
	}
	/**
	 * @param jobName The jobName to set.
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	/**
	 * @return Returns the priority.
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
