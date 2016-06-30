package com.yuanluesoft.jeaf.exception;

/**
 * 
 * @author linchuan
 *
 */
public class UserBusyException extends ServiceException {

	public UserBusyException() {
		super();
	}

	public UserBusyException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserBusyException(String arg0) {
		super(arg0);
	}

	public UserBusyException(Throwable cause) {
		super(cause);
	}
}