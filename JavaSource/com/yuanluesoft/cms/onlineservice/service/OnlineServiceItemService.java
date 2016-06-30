package com.yuanluesoft.cms.onlineservice.service;

import java.util.List;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemGuide;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 办理事项服务
 * @author linchuan
 *
 */
public interface OnlineServiceItemService extends BusinessService {
	//流程类型
	public static final String WORKFLOW_TYPE_ACCEPT = "accept"; //在线受理流程
	public static final String WORKFLOW_TYPE_COMPLAINT = "complaint"; //在线投诉
	public static final String WORKFLOW_TYPE_CONSULT = "consult"; //在线咨询流程
	
	/**
	 * 生成事项编号
	 * @param onlineServiceItem
	 * @param directoryId
	 * @throws ServiceException
	 */
	public void generateItemCode(OnlineServiceItem onlineServiceItem, long directoryId) throws ServiceException;
	
	/**
	 * 获取办理事项(subjections,guide,materials,units,transactors)
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public OnlineServiceItem getOnlineServiceItem(long id) throws ServiceException;
	
	/**
	 * 按目录获取办理事项列表,不含子目录
	 * @param directoryId
	 * @param publicOnly 是否只获取已经公开的
	 * @return
	 * @throws ServiceException
	 */
	public List listOnlineServiceItems(long directoryId, boolean publicOnly) throws ServiceException;
	
	/**
	 * 引入办理事项
	 * @param directoryId
	 * @param importServiceItemIds
	 * @throws ServiceException
	 */
	public void importOnlineServiceItems(long directoryId, String importServiceItemIds) throws ServiceException;

	/**
	 * 拷贝办理事项
	 * @param fromDirectoryId
	 * @param toDirectoryId
	 * @throws ServiceException
	 */
	public void copyServiceItem(long fromDirectoryId, long toDirectoryId) throws ServiceException;
	
	/**
	 * 更新办理事项隶属目录
	 * @param serviceItem
	 * @param isNew
	 * @param subjectionDirectoryIds
	 * @throws ServiceException
	 */
	public void updateServiceItemSubjectios(OnlineServiceItem serviceItem, boolean isNew, String subjectionDirectoryIds) throws ServiceException;
	
	/**
	 * 更新办事指南
	 * @param serviceItem
	 * @param guide
	 * @throws ServiceException
	 */
	public void updateServiceItemGuide(OnlineServiceItem serviceItem, OnlineServiceItemGuide guide) throws ServiceException;
	
	/**
	 * 更新办理机构
	 * @param serviceItem
	 * @param isNew
	 * @param unitIds
	 * @param unitNames
	 * @throws ServiceException
	 */
	public void updateServiceItemUnits(OnlineServiceItem serviceItem, boolean isNew, String unitIds, String unitNames) throws ServiceException;
	
	/**
	 * 更新办理人
	 * @param serviceItem
	 * @param isNew
	 * @param userIds
	 * @param userNames
	 * @throws ServiceException
	 */
	public void updateServiceItemTransactors(OnlineServiceItem serviceItem, boolean isNew, String userIds, String userNames) throws ServiceException;

	/**
	 * 获取流程ID
	 * @param serviceItemId
	 * @param worflowType
	 * @return
	 * @throws ServiceException
	 */
	public String getWorkflowId(long serviceItemId, String worflowType) throws ServiceException;
	
	/**
	 * 获取办理事项的经办人
	 * @param serviceItemId
	 * @return
	 * @throws ServiceException
	 */
	public List listServiceItemTransactors(long serviceItemId) throws ServiceException;
	
	/**
	 * 获取办理事项的所在目录的管理员,继承上级目录的管理员
	 * @param serviceItemId
	 * @return
	 * @throws ServiceException
	 */
	public List listServiceItemManagers(long serviceItemId) throws ServiceException;
	
	/**
	 * 重新将指定目录下的办理事项同步到网站
	 * @param directoryId
	 * @throws ServiceException
	 */
	public void resynchServiceItems(long directoryId) throws ServiceException;
	
	/**
	 * 从文件导入办理事项
	 * @param uploadFiles
	 * @param siteId
	 * @throws ServiceException
	 */
	public void importServiceItems(List uploadFiles) throws ServiceException;
	
	/**
	 * 导入行政职权目录
	 * @param directoryId
	 * @param uploadFiles
	 * @param sheetAsDirectory
	 * @throws ServiceException
	 */
	public void importAuthority(long directoryId, List uploadFiles, boolean sheetAsDirectory) throws ServiceException;
}