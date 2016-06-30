package com.yuanluesoft.cms.pagebuilder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 静态页面:用JS输出的页面元素(cms_static_page_js_element)
 * @author linchuan
 *
 */
public class StaticPageJsElement extends Record {
	private long pageId; //静态页面ID
	private String elementHtml; //页面元素HTML
	private String queryString; //请求参数,用来和请求参数比对
	
	/**
	 * @return the elementHtml
	 */
	public String getElementHtml() {
		return elementHtml;
	}
	/**
	 * @param elementHtml the elementHtml to set
	 */
	public void setElementHtml(String elementHtml) {
		this.elementHtml = elementHtml;
	}
	/**
	 * @return the pageId
	 */
	public long getPageId() {
		return pageId;
	}
	/**
	 * @param pageId the pageId to set
	 */
	public void setPageId(long pageId) {
		this.pageId = pageId;
	}
	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}
	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
}