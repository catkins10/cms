package com.yuanluesoft.bbs.base.forms;

import java.util.List;

import com.yuanluesoft.cms.base.forms.SiteViewForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class BbsViewForm extends SiteViewForm {
	private String loginUrl;
	private String requestUrl;
	private boolean anonymousEnable;
	private List parentDirectories; //上级目录列表

	/**
	 * @return the parentDirectories
	 */
	public List getParentDirectories() {
		return parentDirectories;
	}

	/**
	 * @param parentDirectories the parentDirectories to set
	 */
	public void setParentDirectories(List parentDirectories) {
		this.parentDirectories = parentDirectories;
	}

	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * @return the requestUrl
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	/**
	 * @param requestUrl the requestUrl to set
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	/**
	 * @return the anonymousEnable
	 */
	public boolean isAnonymousEnable() {
		return anonymousEnable;
	}

	/**
	 * @param anonymousEnable the anonymousEnable to set
	 */
	public void setAnonymousEnable(boolean anonymousEnable) {
		this.anonymousEnable = anonymousEnable;
	}
}