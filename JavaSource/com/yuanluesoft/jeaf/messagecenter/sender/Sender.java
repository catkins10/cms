/*
 * Created on 2005-11-26
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender;

import java.io.Serializable;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;

/**
 * 
 * @author linchuan
 *
 */
public abstract class Sender implements Serializable {
	private String name; //发送器名称
	private boolean feedback; //用户收到消息后是否能反馈
	private String description; //描述
	
	/**
	 * 发送消息,发送成功时返回true
	 * @param message
	 * @param feedbackDelay
	 * @return
	 * @throws SendException
	 */
	public abstract boolean sendMessage(Message message, int feedbackDelay) throws SendException;
	
	/**
	 * 停止发送器
	 * @throws SendException
	 */
	public abstract void stopSender() throws SendException;
	
	/**
	 * 加载发送器个人配置
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public abstract Object loadPersonalCustom(long personId) throws ServiceException;
	
	/**
	 * 保存发送器个人配置
	 * @param personId
	 * @param config
	 * @throws ServiceException
	 */
	public abstract void savePersonalCustom(long personId, Object config) throws ServiceException;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the feedback
	 */
	public boolean isFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}