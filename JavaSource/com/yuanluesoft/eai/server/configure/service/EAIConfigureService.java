/*
 * Created on 2006-8-23
 *
 */
package com.yuanluesoft.eai.server.configure.service;

import com.yuanluesoft.eai.server.model.EAI;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author linchuan
 *
 */
public interface EAIConfigureService extends WorkflowConfigureCallback {
	
	/**
	 * 初始化eai配置,如果已经配置过,怎不再重复初始化
	 * @param systemName
	 * @param managerId
	 * @param managerName
	 * @return
	 * @throws ServiceException
	 */
	public boolean initConfigure(String systemName, long managerId, String managerName) throws ServiceException;
    
    /**
     * 加载配置
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    public EAI loadConfigure(SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 保存配置
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    public void saveConfigure(SessionInfo sessionInfo, EAI eai) throws ServiceException;
    
    /**
     * 更新应用配置
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    public void updateApplicationSet(String applicationSetName, String applicationSetXML) throws ServiceException;
}