/*
 * Created on 2005-12-15
 *
 */
package com.yuanluesoft.jeaf.messagecenter.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.messagecenter.sender.msn.pojo.MsnCustom;
import com.yuanluesoft.jeaf.messagecenter.sender.sms.pojo.SmsCustom;

/**
 * 
 * @author linchuan
 *
 */
public class PersonMessageCenterConfig extends ActionForm {
	private List senders; //发送器列表
	private MsnCustom msnCustom = new MsnCustom(); //MSN配置
	private SmsCustom smsCustom = new SmsCustom(); //短信配置
	
	/**
	 * @return Returns the senders.
	 */
	public List getSenders() {
		return senders;
	}
	/**
	 * @param senders The senders to set.
	 */
	public void setSenders(List senders) {
		this.senders = senders;
	}
	/**
	 * @return the msnCustom
	 */
	public MsnCustom getMsnCustom() {
		return msnCustom;
	}
	/**
	 * @param msnCustom the msnCustom to set
	 */
	public void setMsnCustom(MsnCustom msnCustom) {
		this.msnCustom = msnCustom;
	}
	/**
	 * @return the smsCustom
	 */
	public SmsCustom getSmsCustom() {
		return smsCustom;
	}
	/**
	 * @param smsCustom the smsCustom to set
	 */
	public void setSmsCustom(SmsCustom smsCustom) {
		this.smsCustom = smsCustom;
	}
}
