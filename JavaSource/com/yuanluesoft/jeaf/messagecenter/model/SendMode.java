/*
 * Created on 2005-11-28
 *
 */
package com.yuanluesoft.jeaf.messagecenter.model;

import java.io.Serializable;

import com.yuanluesoft.jeaf.messagecenter.sender.Sender;

/**
 * 
 * @author linchuan
 *
 */
public class SendMode implements Serializable {
	private Sender sender; //发送器
	private int feedbackDelay; //反馈等待时间,以秒为单位
	private int minSendHour; //最小发送时间,小于这个时间不发送消息
	private int maxSendHour; //最大发送时间,大于这个时间不发送消息
	
	/**
	 * @return Returns the feedbackDelay.
	 */
	public int getFeedbackDelay() {
		return feedbackDelay;
	}
	/**
	 * @param feedbackDelay The feedbackDelay to set.
	 */
	public void setFeedbackDelay(int feedbackDelay) {
		this.feedbackDelay = feedbackDelay;
	}
	/**
	 * @return Returns the sender.
	 */
	public Sender getSender() {
		return sender;
	}
	/**
	 * @param sender The sender to set.
	 */
	public void setSender(Sender sender) {
		this.sender = sender;
	}
	/**
	 * @return the maxSendHour
	 */
	public int getMaxSendHour() {
		return maxSendHour;
	}
	/**
	 * @param maxSendHour the maxSendHour to set
	 */
	public void setMaxSendHour(int maxSendHour) {
		this.maxSendHour = maxSendHour;
	}
	/**
	 * @return the minSendHour
	 */
	public int getMinSendHour() {
		return minSendHour;
	}
	/**
	 * @param minSendHour the minSendHour to set
	 */
	public void setMinSendHour(int minSendHour) {
		this.minSendHour = minSendHour;
	}
}
