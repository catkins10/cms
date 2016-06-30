package com.yuanluesoft.im.service;

import com.yuanluesoft.im.model.message.Message;

/**
 * 应答消息输出
 * @author linchuan
 *
 */
public interface MessageWriter {
	
	/**
	 * 输出IM服务应答的消息
	 * @param message
	 */
	public void writeResponseMessage(Message message);
}