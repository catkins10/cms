package com.yuanluesoft.jeaf.sms.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 短信服务:单位业务配置(sms_unit_business)
 * @author linchuan
 *
 */
public class SmsUnitBusiness extends Record {
	private long unitConfigId; //单位配置ID
	private long businessId; //业务ID
	private String businessName; //业务名称
	private int chargeMode; //计费方式,固定费用/按条计费
	private double price; //价格
	private double discount; //折扣,如:9.5,默认:10
	private String smsNumber; //短信号码
	private int enabled = 1; //是否启用
	private String postfix; //附加信息格式,如:[<单位名称>农政通]
	private Timestamp lastModified; //最后修改时间
	private long lastModifierId; //最后修改人ID
	private String lastModifier; //最后修改人
	private Set transactors; //办理人,包括短信发送编辑、审核,短信接收受理、审核
	private SmsUnitConfig unitConfig; //单位配置
	
	/**
	 * @return the businessId
	 */
	public long getBusinessId() {
		return businessId;
	}
	/**
	 * @param businessId the businessId to set
	 */
	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}
	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	/**
	 * @return the chargeMode
	 */
	public int getChargeMode() {
		return chargeMode;
	}
	/**
	 * @param chargeMode the chargeMode to set
	 */
	public void setChargeMode(int chargeMode) {
		this.chargeMode = chargeMode;
	}
	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}
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
	/**
	 * @return the postfix
	 */
	public String getPostfix() {
		return postfix;
	}
	/**
	 * @param postfix the postfix to set
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the smsNumber
	 */
	public String getSmsNumber() {
		return smsNumber;
	}
	/**
	 * @param smsNumber the smsNumber to set
	 */
	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}
	/**
	 * @return the transactors
	 */
	public Set getTransactors() {
		return transactors;
	}
	/**
	 * @param transactors the transactors to set
	 */
	public void setTransactors(Set transactors) {
		this.transactors = transactors;
	}
	/**
	 * @return the unitConfigId
	 */
	public long getUnitConfigId() {
		return unitConfigId;
	}
	/**
	 * @param unitConfigId the unitConfigId to set
	 */
	public void setUnitConfigId(long unitConfigId) {
		this.unitConfigId = unitConfigId;
	}
	/**
	 * @return the unitConfig
	 */
	public SmsUnitConfig getUnitConfig() {
		return unitConfig;
	}
	/**
	 * @param unitConfig the unitConfig to set
	 */
	public void setUnitConfig(SmsUnitConfig unitConfig) {
		this.unitConfig = unitConfig;
	}
}