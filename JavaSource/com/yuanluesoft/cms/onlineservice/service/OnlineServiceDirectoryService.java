package com.yuanluesoft.cms.onlineservice.service;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceMainDirectory;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 网上办事目录服务
 * @author linchuan
 *
 */
public interface OnlineServiceDirectoryService extends DirectoryService, WorkflowConfigureCallback {
	
	/**
	 * 获取主目录
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public OnlineServiceMainDirectory getMainDirectory(long directoryId) throws ServiceException;
	
	/**
	 * 按站点ID获取网上办事目录
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public OnlineServiceDirectory getDirectoryBySiteId(long siteId) throws ServiceException;
	
	/**
	 * 同步的网站栏目ID列表
	 * @param directoryIds
	 * @param dataType item/complaint/consult
	 * @return
	 * @throws ServiceException
	 */
	public String getSynchSiteIds(String directoryIds, String dataType) throws ServiceException;
	
	/**
	 * 按目录ID获取流程
	 * @param directoryId
	 * @param worflowType
	 * @return
	 * @throws ServiceException
	 */
	public String getWorkflowId(long directoryId, String worflowType) throws ServiceException;
}