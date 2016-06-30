/*
 * Created on 2004-12-21
 *
 */
package com.yuanluesoft.jeaf.sessionmanage.exception;

/**
 * 
 * @author linchuan
 *
 */
public class SessionException extends Exception {
	public static final String SESSION_NONE = "noSession"; //无会话
	public static final String SESSION_TIMEOUT = "timeout"; //会话超时
	public static final String SESSION_FAILED = "failed"; //创建会话失败
	public static final String SESSION_NO_MATCH = "noMatch"; //会话不匹配
	
	public SessionException(String message) {
		super(message);
	}

	public SessionException(Throwable cause) {
		super(cause);
	}
}
