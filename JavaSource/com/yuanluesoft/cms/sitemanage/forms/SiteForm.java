package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 站点表单
 * @author linchuan
 *
 */
public class SiteForm extends ActionForm {
	private long siteId; //站点ID

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