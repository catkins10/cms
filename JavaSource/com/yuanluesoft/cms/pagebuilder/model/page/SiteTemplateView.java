package com.yuanluesoft.cms.pagebuilder.model.page;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class SiteTemplateView extends CloneableObject {
	private String viewApplication; //模板所在应用
	private String viewName; //模板视图

	/**
	 * @return the viewApplication
	 */
	public String getViewApplication() {
		return viewApplication;
	}


	/**
	 * @param viewApplication the viewApplication to set
	 */
	public void setViewApplication(String viewApplication) {
		this.viewApplication = viewApplication;
	}

	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
}
