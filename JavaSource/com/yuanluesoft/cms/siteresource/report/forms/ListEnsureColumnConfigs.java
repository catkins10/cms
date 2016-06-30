package com.yuanluesoft.cms.siteresource.report.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ListEnsureColumnConfigs extends ActionForm {
	private long siteId;
	private List ensureColumnConfigs;
	/**
	 * @return the ensureColumnConfigs
	 */
	public List getEnsureColumnConfigs() {
		return ensureColumnConfigs;
	}
	/**
	 * @param ensureColumnConfigs the ensureColumnConfigs to set
	 */
	public void setEnsureColumnConfigs(List ensureColumnConfigs) {
		this.ensureColumnConfigs = ensureColumnConfigs;
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