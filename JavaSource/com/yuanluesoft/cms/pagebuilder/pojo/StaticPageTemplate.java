package com.yuanluesoft.cms.pagebuilder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 静态页面:使用到的模板(cms_static_page_template)
 * @author linchuan
 *
 */
public class StaticPageTemplate extends Record {
	private long pageId; //静态页面ID
	private long templateId; //使用的模板ID
	
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
	 * @return the templateId
	 */
	public long getTemplateId() {
		return templateId;
	}
	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
}