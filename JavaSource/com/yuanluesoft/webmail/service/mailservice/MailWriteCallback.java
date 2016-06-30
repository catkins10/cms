package com.yuanluesoft.webmail.service.mailservice;

import java.io.Writer;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.webmail.pojo.Mail;

/**
 * 
 * @author linchuan
 *
 */
public interface MailWriteCallback {
	
	/**
	 * 写邮件
	 * @param writer
	 * @param mail
	 * @throws ServiceException
	 */
	public void writeMail(Writer writer, Mail mail) throws ServiceException;
}