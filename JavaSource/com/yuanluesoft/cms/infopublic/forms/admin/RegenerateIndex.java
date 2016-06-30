package com.yuanluesoft.cms.infopublic.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class RegenerateIndex extends ActionForm {
	private long mainDirectoryId;

	/**
	 * @return the mainDirectoryId
	 */
	public long getMainDirectoryId() {
		return mainDirectoryId;
	}

	/**
	 * @param mainDirectoryId the mainDirectoryId to set
	 */
	public void setMainDirectoryId(long mainDirectoryId) {
		this.mainDirectoryId = mainDirectoryId;
	}
}