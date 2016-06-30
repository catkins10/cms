package com.yuanluesoft.cms.siteresource.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 静态页面处理:模板引用到的站点资源列表(cms_template_resource_list)
 * @author linchuan
 *
 */
public class TemplateResourceList extends Record {
	private long templateId; //模板ID
	private long siteId; //资源隶属栏目ID
	
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