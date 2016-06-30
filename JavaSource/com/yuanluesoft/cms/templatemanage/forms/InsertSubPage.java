package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class InsertSubPage extends ActionForm {
	private String applicationName; //URL参数:应用名称
	private String pageName; //URL参数: 表单名称
	private int themeType; //URL参数:模板类型
	
	private String predefinedPage; //预置的页面
	private String subPageName; //子页面名称
	
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the subPageName
	 */
	public String getSubPageName() {
		return subPageName;
	}
	/**
	 * @param subPageName the subPageName to set
	 */
	public void setSubPageName(String subPageName) {
		this.subPageName = subPageName;
	}
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the predefinedPage
	 */
	public String getPredefinedPage() {
		return predefinedPage;
	}
	/**
	 * @param predefinedPage the predefinedPage to set
	 */
	public void setPredefinedPage(String predefinedPage) {
		this.predefinedPage = predefinedPage;
	}
	/**
	 * @return the themeType
	 */
	public int getThemeType() {
		return themeType;
	}
	/**
	 * @param themeType the themeType to set
	 */
	public void setThemeType(int themeType) {
		this.themeType = themeType;
	}
}