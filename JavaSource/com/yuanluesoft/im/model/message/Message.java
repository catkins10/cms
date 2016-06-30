package com.yuanluesoft.im.model.message;

import java.io.Serializable;

/**
 * IM消息
 * @author linchuan
 *
 */
public class Message implements Serializable {
	public static final byte CMD_LOGIN = 0x01; //登录
	public static final byte CMD_LOGIN_ACK = 0x02; //登录应答
	public static final byte CMD_LOGOFF = 0x03; //客户端登录后;通知其他客户端注销
	public static final byte CMD_EXIT_CLIENT = 0x05; //退出客户端
	public static final byte CMD_KEEP_ALIVE = 0x06; //心跳
	public static final byte CMD_PERSON_STATUS_CHANGED = 0x08; //用户改变状态

	public static final byte CMD_CREATE_CHAT = 0x10; //创建对话
	public static final byte CMD_CREATE_GROUP_CHAT = 0x11; //创建多人对话
	public static final byte CMD_CHAT_DETAIL_REQUEST = 0x12; //请求获取对话详情
	public static final byte CMD_CHAT_DETAIL = 0x13; //对话详情
	
	public static final byte CMD_TALK_SUBMIT = 0x20; //提交发言
	public static final byte CMD_TALK = 0x21; //对话对象发言
	public static final byte CMD_TALK_DETAIL_REQUEST = 0x22; //请求获取发言内容
	public static final byte CMD_TALK_DETAIL = 0x23; //发言内容列表;是对public static final byte CMD_TALK_RETRIEVE_DETAIL的应答
	
	public static final byte CMD_SYSTEM_MESSAGE = 0x30; //系统消息通知
	public static final byte CMD_SYSTEM_MESSAGE_DETAIL_REQUEST = 0x31; //请求获取系统消息内容
	public static final byte CMD_SYSTEM_MESSAGE_DETAIL = 0x32; //系统消息内容
	public static final byte CMD_SYSTEM_MESSAGE_FEEDBACK = 0x33; //系统消息反馈
	
	public static final byte CMD_FILE_TRANSFER_REQUEST = 0x50; //文件传送请求
	public static final byte CMD_FILE_TRANSFER_ACK = 0x51; //文件传送请求应答
	public static final byte CMD_FILE_TRANSFER_CANCEL = 0x52; //放弃文件传送请求
	public static final byte CMD_FILE_TRANSFER_COMPLETE = 0x53; //文件传送完成
	
	private byte command; //消息类型

	/**
	 * @return the command
	 */
	public byte getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(byte command) {
		this.command = command;
	}
}