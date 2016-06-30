package com.yuanluesoft.jeaf.usermanage.forms;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ListToApprovalStudents extends ViewForm {
	private boolean isPass; //是否审批通过

	/**
	 * @return the isPass
	 */
	public boolean isPass() {
		return isPass;
	}

	/**
	 * @param isPass the isPass to set
	 */
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
}