/*
 * Created on 2005-1-14
 *
 */
package com.yuanluesoft.jeaf.exception;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author linchuan
 *
 */
public class ValidateException extends ServiceException {
	private List invalidElementIds;
	
	public ValidateException() {
		super();
	}
	
	public ValidateException(String message) {
		super(message);
	}
	
	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ValidateException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @param message
	 */
	public ValidateException(String message, String id) {
		super(message);
		invalidElementIds = new ArrayList();
		invalidElementIds.add(id);
	}
	
	public ValidateException(String message, List invalidElementIds) {
		super(message);
		this.invalidElementIds = invalidElementIds;
	}
	
	/**
	 * @return Returns the invalidElementIds.
	 */
	public List getInvalidElementIds() {
		return invalidElementIds;
	}
	/**
	 * @param invalidElementIds The invalidElementIds to set.
	 */
	public void setInvalidElementIds(List invalidElementIds) {
		this.invalidElementIds = invalidElementIds;
	}
}