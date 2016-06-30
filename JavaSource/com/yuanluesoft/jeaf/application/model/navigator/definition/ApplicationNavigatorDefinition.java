package com.yuanluesoft.jeaf.application.model.navigator.definition;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 应用导航定义(navigator-config.xml)
 * @author linchuan
 *
 */
public class ApplicationNavigatorDefinition extends CloneableObject {
	private String navigatorServiceName; //使用的服务名称
	private String navigatorURL; //自定义导航页面的URL
	private List links; //链接定义
	
	/**
	 * @return the links
	 */
	public List getLinks() {
		return links;
	}
	/**
	 * @param links the links to set
	 */
	public void setLinks(List links) {
		this.links = links;
	}
	/**
	 * @return the navigatorServiceName
	 */
	public String getNavigatorServiceName() {
		return navigatorServiceName;
	}
	/**
	 * @param navigatorServiceName the navigatorServiceName to set
	 */
	public void setNavigatorServiceName(String navigatorServiceName) {
		this.navigatorServiceName = navigatorServiceName;
	}
	/**
	 * @return the navigatorURL
	 */
	public String getNavigatorURL() {
		return navigatorURL;
	}
	/**
	 * @param navigatorURL the navigatorURL to set
	 */
	public void setNavigatorURL(String navigatorURL) {
		this.navigatorURL = navigatorURL;
	}
}