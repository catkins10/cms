package com.yuanluesoft.appraise.appraiser.forms.admin;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */

public class ImportAppraiser extends ActionForm {
	private long orgId; //地区ID
	private String orgName; //地区名称
	private Date expire; //有效期
	private int appraiserType; //评议员类型

	/**
	 * @return the expire
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * @param expire the expire to set
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
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
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return the appraiserType
	 */
	public int getAppraiserType() {
		return appraiserType;
	}

	/**
	 * @param appraiserType the appraiserType to set
	 */
	public void setAppraiserType(int appraiserType) {
		this.appraiserType = appraiserType;
	}
}