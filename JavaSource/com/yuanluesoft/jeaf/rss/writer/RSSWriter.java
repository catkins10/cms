package com.yuanluesoft.jeaf.rss.writer;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.rss.model.RSSChannel;

/**
 * 
 * @author linchuan
 *
 */
public interface RSSWriter {
	
	/**
	 * 输出一个RSS频道
	 * @param channel
	 * @param response
	 * @throws ServiceException
	 */
	public void writeRSSChannel(RSSChannel channel, HttpServletResponse response) throws ServiceException;
}