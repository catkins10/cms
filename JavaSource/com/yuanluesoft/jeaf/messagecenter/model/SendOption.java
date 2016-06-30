/*
 * Created on 2005-11-27
 *
 */
package com.yuanluesoft.jeaf.messagecenter.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class SendOption implements Serializable {
	private String priority; //优先级
	private int retry; 		//发送失败后重试次数
	private int retryDelay; //每次重试的时间间隔
	private List sendModes; //发送方式列表
	
	/**
	 * @return Returns the priority.
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the retry.
	 */
	public int getRetry() {
		return retry;
	}
	/**
	 * @param retry The retry to set.
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}
	/**
	 * @return Returns the sendModes.
	 */
	public List getSendModes() {
		return sendModes;
	}
	/**
	 * @param sendModes The sendModes to set.
	 */
	public void setSendModes(List sendModes) {
		this.sendModes = sendModes;
	}
	/**
	 * @return Returns the retryDelay.
	 */
	public int getRetryDelay() {
		return retryDelay;
	}
	/**
	 * @param retryDelay The retryDelay to set.
	 */
	public void setRetryDelay(int retryDelay) {
		this.retryDelay = retryDelay;
	}
}
