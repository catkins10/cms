package com.yuanluesoft.im.model.message;

/**
 * 心跳
 * @author linchuan
 *
 */
public class KeepAlive extends Message {

	public KeepAlive() {
		setCommand(CMD_KEEP_ALIVE);
	}
}