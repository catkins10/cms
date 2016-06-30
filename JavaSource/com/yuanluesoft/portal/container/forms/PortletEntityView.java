package com.yuanluesoft.portal.container.forms;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class PortletEntityView extends ViewForm {
	private long siteId = -1; //站点ID
	private long orgId = -1; //组织机构ID
	private String category; //修改分类

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
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}