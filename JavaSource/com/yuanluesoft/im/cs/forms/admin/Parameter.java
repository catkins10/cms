package com.yuanluesoft.im.cs.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Parameter extends ActionForm {
	private long siteId; //站点ID
	private String welcome; //欢迎辞,如：您好，我是客服001号，请问有什么可以帮助您！
	private int chatTimeout; //客服对话超时时限,以对话的最后发言时间为基准，如果超时了，则不计入客服人员的并发对话数
	private String evaluateLevels; //评价等级,如：非常满意，满意，一般，不满意
	/**
	 * @return the chatTimeout
	 */
	public int getChatTimeout() {
		return chatTimeout;
	}
	/**
	 * @param chatTimeout the chatTimeout to set
	 */
	public void setChatTimeout(int chatTimeout) {
		this.chatTimeout = chatTimeout;
	}
	/**
	 * @return the evaluateLevels
	 */
	public String getEvaluateLevels() {
		return evaluateLevels;
	}
	/**
	 * @param evaluateLevels the evaluateLevels to set
	 */
	public void setEvaluateLevels(String evaluateLevels) {
		this.evaluateLevels = evaluateLevels;
	}
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
	/**
	 * @return the welcome
	 */
	public String getWelcome() {
		return welcome;
	}
	/**
	 * @param welcome the welcome to set
	 */
	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}
}