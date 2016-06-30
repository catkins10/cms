package com.yuanluesoft.cms.pagebuilder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 静态页面:使用到的记录列表(cms_static_page_record_list)
 * @author linchuan
 *
 */
public class StaticPageRecordList extends Record {
	private long pageId; //页面ID
	private String recordClassName; //记录类名称
	private String processorClassName; //处理器类名称
	private long siteId; //隶属的站点/栏目ID
	
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
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
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
	/**
	 * @return the processorClassName
	 */
	public String getProcessorClassName() {
		return processorClassName;
	}
	/**
	 * @param processorClassName the processorClassName to set
	 */
	public void setProcessorClassName(String processorClassName) {
		this.processorClassName = processorClassName;
	}
}