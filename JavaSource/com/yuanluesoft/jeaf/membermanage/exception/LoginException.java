/*
 * Created on 2006-3-10
 *
 */
package com.yuanluesoft.jeaf.membermanage.exception;

/**
 * 
 * @author linchuan
 *
 */
public class LoginException extends Exception {
	
	/**
	 * 
	 */
	public LoginException() {
		super();
	}
	
	/**
	 * @param message
	 */
	public LoginException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 
	 * @param cause
	 */
	public LoginException(Throwable cause) {
		super(cause);
	}
}