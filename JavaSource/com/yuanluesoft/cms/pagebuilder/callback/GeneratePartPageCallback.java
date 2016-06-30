package com.yuanluesoft.cms.pagebuilder.callback;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 回调:生成局部页面
 * @author linchuan
 *
 */
public interface GeneratePartPageCallback {

	/**
	 * 获取局部模板
	 * @param template
	 * @return
	 * @throws ServiceException
	 */
	public HTMLElement getPartTemplate(HTMLDocument template) throws ServiceException;
}