package com.yuanluesoft.cms.capture.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class StartupTasks extends ActionForm {
	private List startupTasks; //任务列表,供选择

	/**
	 * @return the startupTasks
	 */
	public List getStartupTasks() {
		return startupTasks;
	}

	/**
	 * @param startupTasks the startupTasks to set
	 */
	public void setStartupTasks(List startupTasks) {
		this.startupTasks = startupTasks;
	}
}