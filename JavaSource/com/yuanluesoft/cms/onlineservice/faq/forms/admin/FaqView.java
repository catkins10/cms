package com.yuanluesoft.cms.onlineservice.faq.forms.admin;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class FaqView extends ViewForm {
	private long directoryId; //目录ID

	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}

	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
}