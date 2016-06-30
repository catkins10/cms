package com.yuanluesoft.cms.smssubscription.service;

/**
 * 短信内容回调
 * @author linchuan
 *
 */
public interface SmsContentCallback {

	/**
	 * 根据订阅参数判断是否允许发送
	 * @param subscribeParameter
	 * @return
	 */
	public boolean isSendable(String subscribeParameter);
}