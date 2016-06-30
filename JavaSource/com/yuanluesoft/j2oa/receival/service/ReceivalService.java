/*
 * Created on 2006-8-25
 *
 */
package com.yuanluesoft.j2oa.receival.service;

import com.yuanluesoft.archives.administrative.model.FilingOption;
import com.yuanluesoft.j2oa.receival.pojo.Receival;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalFilingConfig;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalTemplateConfig;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface ReceivalService extends BusinessService {
    
	/**
	 * 归档
	 * @param receival
	 * @param handling
	 * @param filingConfig
	 * @param filingPackage
	 * @param filingPersonId
	 * @param filingPersonName
	 * @throws ServiceException
	 */
	public void filing(Receival receival, String handling, ReceivalFilingConfig filingConfig, FilingOption filingOption, long filingPersonId, String filingPersonName) throws ServiceException;
	
	/**
	 * 获取归档配置
	 * @return
	 * @throws ServiceException
	 */
	public ReceivalFilingConfig getFilingConfig() throws ServiceException;
	
	/**
	 * 获取模板配置
	 * @return
	 * @throws ServiceException
	 */
	public ReceivalTemplateConfig getReceivalTemplateConfig() throws ServiceException;
}