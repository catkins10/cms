/*
 * Created on 2007-7-1
 *
 */
package com.yuanluesoft.enterprise.iso.service;

import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author linchuan
 * 
 */
public interface IsoDirectoryService extends DirectoryService, InitializeService, WorkflowConfigureCallback {

	/**
	 * 获取流程ID
	 * @param directoryId
	 * @param workflowType
	 * @return
	 * @throws ServiceException
	 */
	public String getWorkflowId(long directoryId, String workflowType) throws ServiceException;
	
	/**
	 * 获取编号规则
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public String getDocumentNumberingRule(long directoryId) throws ServiceException;
}