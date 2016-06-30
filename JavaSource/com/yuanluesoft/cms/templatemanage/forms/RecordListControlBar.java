package com.yuanluesoft.cms.templatemanage.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class RecordListControlBar extends ActionForm {
	private List controlBarStyles; //预置风格列表

	/**
	 * @return the controlBarStyles
	 */
	public List getControlBarStyles() {
		return controlBarStyles;
	}

	/**
	 * @param controlBarStyles the controlBarStyles to set
	 */
	public void setControlBarStyles(List controlBarStyles) {
		this.controlBarStyles = controlBarStyles;
	}
}