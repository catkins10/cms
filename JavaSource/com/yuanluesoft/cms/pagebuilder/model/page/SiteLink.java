package com.yuanluesoft.cms.pagebuilder.model.page;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 站点链接
 * @author linchuan
 *
 */
public class SiteLink extends CloneableObject {
	private String title; //名称
	private String url;
	private String dialogURL; //配置对话框,默认是/cms/templatemanage/insertPageLink.shtml
	
	/**
	 * @return the dialogURL
	 */
	public String getDialogURL() {
		return dialogURL;
	}
	/**
	 * @param dialogURL the dialogURL to set
	 */
	public void setDialogURL(String dialogURL) {
		this.dialogURL = dialogURL;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
