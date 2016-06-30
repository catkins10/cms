package com.yuanluesoft.cms.infopublic.forms.admin;

/**
 * 
 * @author linchuan
 *
 */
public class ArticleDirectory extends Directory {
	private String redirectUrl; //重定向URL

	/**
	 * @return the redirectUrl
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * @param redirectUrl the redirectUrl to set
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}