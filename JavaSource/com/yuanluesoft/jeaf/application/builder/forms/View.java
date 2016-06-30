package com.yuanluesoft.jeaf.application.builder.forms;

import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationView;

/**
 * 
 * @author linchuan
 *
 */
public class View extends ApplicationForm {
	private ApplicationView view = new ApplicationView();
	private boolean workflowSupport; //是否支持工作流

	/**
	 * @return the view
	 */
	public ApplicationView getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(ApplicationView view) {
		this.view = view;
	}

	/**
	 * @return the workflowSupport
	 */
	public boolean isWorkflowSupport() {
		return workflowSupport;
	}

	/**
	 * @param workflowSupport the workflowSupport to set
	 */
	public void setWorkflowSupport(boolean workflowSupport) {
		this.workflowSupport = workflowSupport;
	}
}