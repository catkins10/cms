/*
 * Created on 2004-12-18
 *
 */
package com.yuanluesoft.jeaf.exception;

/**
 * 
 * @author linchuan
 *
 */
public class PrivilegeException extends Exception {

	public PrivilegeException() {
		super();
	}
	
	/**
	 * @param arg0
	 */
	public PrivilegeException(String arg0) {
		super(arg0);
	}

	public PrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrivilegeException(Throwable cause) {
		super(cause);
	}
}
