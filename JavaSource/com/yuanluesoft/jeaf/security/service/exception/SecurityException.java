/*
 * Created on 2006-5-17
 *
 */
package com.yuanluesoft.jeaf.security.service.exception;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class SecurityException extends ServiceException {

    public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}
	
    public SecurityException(Throwable cause) {
		super(cause);
	}
	
    public SecurityException() {
        super();
    }
    
    public SecurityException(String message) {
        super(message);
    }
}