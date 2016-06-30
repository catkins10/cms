package com.yuanluesoft.jeaf.struts.service;

import org.apache.struts.config.ModuleConfig;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface StrutsDefineService {
	
	/**
	 * 保存STRUTS配置
	 * @param strutsConfig
	 * @param applicationName
	 * @throws ServiceException
	 */
	public void saveStrutsConfig(ModuleConfig strutsConfig, String applicationName) throws ServiceException;
	
	/**
	 * 注册应用
	 * @param applicationName
	 * @throws ServiceException
	 */
	public void registApplication(String applicationName) throws ServiceException;
}