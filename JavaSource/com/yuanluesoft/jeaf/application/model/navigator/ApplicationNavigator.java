package com.yuanluesoft.jeaf.application.model.navigator;

import java.io.Serializable;
import java.util.List;

/**
 * 应用程序导航
 * @author linchuan
 *
 */
public class ApplicationNavigator implements Serializable {
	private List links; //链接列表
	private String redirect; //重定向到地址

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
	 * @return the redirect
	 */
	public String getRedirect() {
		return redirect;
	}

	/**
	 * @param redirect the redirect to set
	 */
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}