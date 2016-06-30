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
public class ServiceException extends Exception {

	public ServiceException() {
		super();
	}
	
	public ServiceException(String arg0) {
		super(arg0);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}