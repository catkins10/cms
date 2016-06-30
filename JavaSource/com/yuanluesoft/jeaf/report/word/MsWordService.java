package com.yuanluesoft.jeaf.report.word;

import java.io.OutputStream;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface MsWordService {
	
	/**
	 * HTML转换为PDF输出
	 * @param htmlFileName
	 * @param outputStream
	 * @throws ServiceException
	 */
	public abstract void htmlToWordDocument(String htmlFileName, OutputStream outputStream) throws ServiceException;
}