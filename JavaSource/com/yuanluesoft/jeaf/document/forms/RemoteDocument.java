package com.yuanluesoft.jeaf.document.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author chuan
 *
 */
public class RemoteDocument extends ActionForm {
	private String taskId; //任务ID

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}