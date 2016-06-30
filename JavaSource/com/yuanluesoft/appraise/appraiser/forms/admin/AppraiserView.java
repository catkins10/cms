package com.yuanluesoft.appraise.appraiser.forms.admin;

import com.yuanluesoft.jeaf.application.forms.ApplicationView;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiserView extends ApplicationView {
	private long orgId; //组织机构ID
	private boolean direct; //是否市直、县直

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
	 * @return the direct
	 */
	public boolean isDirect() {
		return direct;
	}

	/**
	 * @param direct the direct to set
	 */
	public void setDirect(boolean direct) {
		this.direct = direct;
	}
}