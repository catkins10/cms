package com.yuanluesoft.jeaf.sms.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SmsUnitConfig extends ActionForm {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private int enabled = 1; //是否启用
	private String smsClientName; //短信客户端名称
	private Timestamp lastModified; //最后修改时间
	private long lastModifierId; //最后修改人ID
	private String lastModifier; //最后修改人
	private Set businessConfigs; //业务配置
	
	/**
	 * @return the enabled
	 */
	public int getEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the smsClientName
	 */
	public String getSmsClientName() {
		return smsClientName;
	}
	/**
	 * @param smsClientName the smsClientName to set
	 */
	public void setSmsClientName(String smsClientName) {
		this.smsClientName = smsClientName;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the businessConfigs
	 */
	public Set getBusinessConfigs() {
		return businessConfigs;
	}
	/**
	 * @param businessConfigs the businessConfigs to set
	 */
	public void setBusinessConfigs(Set businessConfigs) {
		this.businessConfigs = businessConfigs;
	}
	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * @return the lastModifier
	 */
	public String getLastModifier() {
		return lastModifier;
	}
	/**
	 * @param lastModifier the lastModifier to set
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	/**
	 * @return the lastModifierId
	 */
	public long getLastModifierId() {
		return lastModifierId;
	}
	/**
	 * @param lastModifierId the lastModifierId to set
	 */
	public void setLastModifierId(long lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
}