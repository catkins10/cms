package com.yuanluesoft.im.model.message;



/**
 * 退出客户端命令
 * @author linchuan
 *
 */
public class ExitClient extends Message {

	public ExitClient() {
		setCommand(CMD_EXIT_CLIENT);
	}
}