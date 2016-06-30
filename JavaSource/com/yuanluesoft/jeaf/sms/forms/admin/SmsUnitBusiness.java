package com.yuanluesoft.jeaf.sms.forms.admin;

import com.yuanluesoft.jeaf.security.model.RecordVisitorList;


/**
 * 
 * @author linchuan
 *
 */
public class SmsUnitBusiness extends SmsUnitConfig {
	private com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness unitBusiness = new com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness();
	private RecordVisitorList smsSendEditors = new RecordVisitorList(); //短信发送编辑
	private RecordVisitorList smsSendAuditors = new RecordVisitorList(); //短信发送审核
	private RecordVisitorList smsReceiveAccepters = new RecordVisitorList(); //短信接收受理
	private RecordVisitorList smsReceiveAuditors = new RecordVisitorList(); //短信接收审核
	private boolean sendPopedomConfig; //是否需要发送权限配置
	private boolean receivePopedomConfig; //是否需要接收权限配置
	
	/**
	 * @return the unitBusiness
	 */
	public com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness getUnitBusiness() {
		return unitBusiness;
	}

	/**
	 * @param unitBusiness the unitBusiness to set
	 */
	public void setUnitBusiness(
			com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness unitBusiness) {
		this.unitBusiness = unitBusiness;
	}

	/**
	 * @return the smsReceiveAccepters
	 */
	public RecordVisitorList getSmsReceiveAccepters() {
		return smsReceiveAccepters;
	}

	/**
	 * @param smsReceiveAccepters the smsReceiveAccepters to set
	 */
	public void setSmsReceiveAccepters(RecordVisitorList smsReceiveAccepters) {
		this.smsReceiveAccepters = smsReceiveAccepters;
	}

	/**
	 * @return the smsReceiveAuditors
	 */
	public RecordVisitorList getSmsReceiveAuditors() {
		return smsReceiveAuditors;
	}

	/**
	 * @param smsReceiveAuditors the smsReceiveAuditors to set
	 */
	public void setSmsReceiveAuditors(RecordVisitorList smsReceiveAuditors) {
		this.smsReceiveAuditors = smsReceiveAuditors;
	}

	/**
	 * @return the smsSendAuditors
	 */
	public RecordVisitorList getSmsSendAuditors() {
		return smsSendAuditors;
	}

	/**
	 * @param smsSendAuditors the smsSendAuditors to set
	 */
	public void setSmsSendAuditors(RecordVisitorList smsSendAuditors) {
		this.smsSendAuditors = smsSendAuditors;
	}

	/**
	 * @return the smsSendEditors
	 */
	public RecordVisitorList getSmsSendEditors() {
		return smsSendEditors;
	}

	/**
	 * @param smsSendEditors the smsSendEditors to set
	 */
	public void setSmsSendEditors(RecordVisitorList smsSendEditors) {
		this.smsSendEditors = smsSendEditors;
	}

	/**
	 * @return the receivePopedomConfig
	 */
	public boolean isReceivePopedomConfig() {
		return receivePopedomConfig;
	}

	/**
	 * @param receivePopedomConfig the receivePopedomConfig to set
	 */
	public void setReceivePopedomConfig(boolean receivePopedomConfig) {
		this.receivePopedomConfig = receivePopedomConfig;
	}

	/**
	 * @return the sendPopedomConfig
	 */
	public boolean isSendPopedomConfig() {
		return sendPopedomConfig;
	}

	/**
	 * @param sendPopedomConfig the sendPopedomConfig to set
	 */
	public void setSendPopedomConfig(boolean sendPopedomConfig) {
		this.sendPopedomConfig = sendPopedomConfig;
	}
}