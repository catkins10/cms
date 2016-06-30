package com.yuanluesoft.im.model.message;

/**
 * 客户端注销命令,当新的客户端登录时,通知其他客户端注销
 * @author linchuan
 *
 */
public class Logoff extends Message {

	public Logoff() {
		setCommand(CMD_LOGOFF);
	}
}