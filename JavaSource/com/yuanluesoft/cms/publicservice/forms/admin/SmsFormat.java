package com.yuanluesoft.cms.publicservice.forms.admin;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceConfigForm;


/**
 * 短信格式配置
 * @author linchuan
 *
 */
public class SmsFormat extends PublicServiceConfigForm {
	private String smsFormat; //短信格式

	/**
	 * @return the smsFormat
	 */
	public String getSmsFormat() {
		return smsFormat;
	}

	/**
	 * @param smsFormat the smsFormat to set
	 */
	public void setSmsFormat(String smsFormat) {
		this.smsFormat = smsFormat;
	}
}