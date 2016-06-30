package com.yuanluesoft.cms.sitemanage.forms;

import java.util.List;

/**
 * 站点管理:视图引用(cms_reference)
 * @author linchuan
 *
 */
public class Reference extends WebDirectory {
	private String viewTitle; //引用的视图描述
	private String applicationName; //引用的视图所在应用
	private String viewName; //引用的视图名称
	private String referenceParameter; //参数
	private String referenceDescription; //参数说明
	
	//拓展参数
	private List siteReferenceConfigureJs; //参数配置时需要引用的脚本
	private String siteReferenceConfigure; //参数配置命令
	
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
	/**
	 * @return the siteReferenceConfigure
	 */
	public String getSiteReferenceConfigure() {
		return siteReferenceConfigure;
	}
	/**
	 * @param siteReferenceConfigure the siteReferenceConfigure to set
	 */
	public void setSiteReferenceConfigure(String siteReferenceConfigure) {
		this.siteReferenceConfigure = siteReferenceConfigure;
	}
	/**
	 * @return the siteReferenceConfigureJs
	 */
	public List getSiteReferenceConfigureJs() {
		return siteReferenceConfigureJs;
	}
	/**
	 * @param siteReferenceConfigureJs the siteReferenceConfigureJs to set
	 */
	public void setSiteReferenceConfigureJs(List siteReferenceConfigureJs) {
		this.siteReferenceConfigureJs = siteReferenceConfigureJs;
	}
}