package com.yuanluesoft.job.company.forms.admin;

import com.yuanluesoft.jeaf.directorymanage.forms.DirectoryForm;

/**
 * 
 * @author linchuan
 *
 */
public class JobParameter extends DirectoryForm {
	private String childParameters; //批量添加的参数列表

	/**
	 * @return the childParameters
	 */
	public String getChildParameters() {
		return childParameters;
	}

	/**
	 * @param childParameters the childParameters to set
	 */
	public void setChildParameters(String childParameters) {
		this.childParameters = childParameters;
	}
}