/*
 * Created on 2004-12-20
 *
 */
package com.yuanluesoft.jeaf.database.exception;

/**
 * 
 * @author linchuan
 *
 */
public class DataAccessException extends org.springframework.dao.DataAccessException {
	/**
	 * @param arg0
	 */
	public DataAccessException(String arg0) {
		super(arg0);
	}
}