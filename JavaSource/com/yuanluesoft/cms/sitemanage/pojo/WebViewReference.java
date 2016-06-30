package com.yuanluesoft.cms.sitemanage.pojo;

/**
 * 站点管理:视图引用(cms_reference)
 * @author linchuan
 *
 */
public class WebViewReference extends WebDirectory {
	private String viewTitle; //引用的视图描述
	private String applicationName; //引用的视图所在应用
	private String viewName; //引用的视图名称
	private String referenceParameter; //参数配置,如果是参数直接被写入hql,且参数类型是字符,需要加入单引号
	private String referenceDescription; //参数说明
	
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
	 * @return the referenceDescription
	 */
	public String getReferenceDescription() {
		return referenceDescription;
	}
	/**
	 * @param referenceDescription the referenceDescription to set
	 */
	public void setReferenceDescription(String referenceDescription) {
		this.referenceDescription = referenceDescription;
	}
	/**
	 * @return the referenceParameter
	 */
	public String getReferenceParameter() {
		return referenceParameter;
	}
	/**
	 * @param referenceParameter the referenceParameter to set
	 */
	public void setReferenceParameter(String referenceParameter) {
		this.referenceParameter = referenceParameter;
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
	/**
	 * @return the viewTitle
	 */
	public String getViewTitle() {
		return viewTitle;
	}
	/**
	 * @param viewTitle the viewTitle to set
	 */
	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}
}