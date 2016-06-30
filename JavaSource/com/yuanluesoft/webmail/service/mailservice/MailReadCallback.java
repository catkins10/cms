package com.yuanluesoft.webmail.service.mailservice;

import java.io.Reader;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface MailReadCallback {
	
	/**
	 * 读邮件
	 * @param reader
	 * @throws ServiceException
	 */
	public void readMail(Reader reader) throws ServiceException;
}