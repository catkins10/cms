package com.yuanluesoft.jeaf.wap;

import java.io.Writer;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * WAP页面生成器
 * @author linchuan
 *
 */
public interface WapPageService {

	/**
	 * 输出wap页面
	 * @param htmlPage
	 * @param writer
	 * @throws ServiceException
	 */
	public void writeWapPage(String htmlPage, Writer writer) throws ServiceException;
}
