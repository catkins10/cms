package com.yuanluesoft.cms.infopublic.pojo;


/**
 * 信息公开:文章目录(public_article_directory)
 * @author linchuan
 *
 */
public class PublicArticleDirectory extends PublicDirectory {
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