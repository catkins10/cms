package com.yuanluesoft.cms.pagebuilder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 静态页面:使用到的页面元素(不含记录列表)(cms_static_page_element)
 * @author linchuan
 *
 */

public class StaticPageElement extends Record {
	private long pageId; //页面ID
	private String elementName; //页面元素名称
	private long siteId; //隶属的站点/栏目ID
	
	/**
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}
	/**
	 * @param elementName the elementName to set
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
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
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}