package com.yuanluesoft.cms.siteresource.report.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ListEnsureUnitCategories extends ActionForm {
	private long siteId;
	private List ensureUnitCategories;
	
	/**
	 * @return the ensureUnitCategories
	 */
	public List getEnsureUnitCategories() {
		return ensureUnitCategories;
	}
	/**
	 * @param ensureUnitCategories the ensureUnitCategories to set
	 */
	public void setEnsureUnitCategories(List ensureUnitCategories) {
		this.ensureUnitCategories = ensureUnitCategories;
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