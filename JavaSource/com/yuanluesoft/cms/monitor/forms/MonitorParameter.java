package com.yuanluesoft.cms.monitor.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorParameter extends ActionForm {
	private long orgId; //适用的机构ID
	private String orgName; //适用的机构名称
	private Set contentParameters; //采集对象配置
		
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
	 * @return the contentParameters
	 */
	public Set getContentParameters() {
		return contentParameters;
	}
	/**
	 * @param contentParameters the contentParameters to set
	 */
	public void setContentParameters(Set contentParameters) {
		this.contentParameters = contentParameters;
	}
}