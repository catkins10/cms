package com.yuanluesoft.cms.capture.model;

/**
 * 被抓取的源页面
 * @author linchuan
 *
 */
public class SourcePage {
	private String listPageURL; //记录列表页面URL
	private String listPageHTML; //记录列表页面HTML
	private String contentPageURL; //内容页面URL
	private String contentPageHTML; //内容页面HTML
	
	/**
	 * @return the contentPageHTML
	 */
	public String getContentPageHTML() {
		return contentPageHTML;
	}
	/**
	 * @param contentPageHTML the contentPageHTML to set
	 */
	public void setContentPageHTML(String contentPageHTML) {
		this.contentPageHTML = contentPageHTML;
	}
	/**
	 * @return the contentPageURL
	 */
	public String getContentPageURL() {
		return contentPageURL;
	}
	/**
	 * @param contentPageURL the contentPageURL to set
	 */
	public void setContentPageURL(String contentPageURL) {
		this.contentPageURL = contentPageURL;
	}
	/**
	 * @return the listPageHTML
	 */
	public String getListPageHTML() {
		return listPageHTML;
	}
	/**
	 * @param listPageHTML the listPageHTML to set
	 */
	public void setListPageHTML(String listPageHTML) {
		this.listPageHTML = listPageHTML;
	}
	/**
	 * @return the listPageURL
	 */
	public String getListPageURL() {
		return listPageURL;
	}
	/**
	 * @param listPageURL the listPageURL to set
	 */
	public void setListPageURL(String listPageURL) {
		this.listPageURL = listPageURL;
	}
}