package com.yuanluesoft.cms.pagebuilder.parser;

import com.yuanluesoft.cms.pagebuilder.model.page.SiteApplication;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 页面配置解析器
 * @author linchuan
 *
 */
public interface PageDefineParser {

	/**
	 * 解析站点应用配置
	 * @param applicationName
	 * @param fileName
	 * @return
	 * @throws ServiceException
	 */
	public SiteApplication parseSiteApplication(String applicationName, String fileName) throws ParseException;
	
	/**
	 * 保存页面定义
	 * @param siteApplication
	 * @param defineFilePath
	 * @throws ServiceException
	 */
	public void savePageDefine(SiteApplication siteApplication, String defineFilePath) throws ParseException;
}
